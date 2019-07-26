package org.aztec.deadsea.xa.impl.redis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aztec.autumn.common.utils.CacheException;
import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.cache.CacheDataSubscriber;
import org.aztec.deadsea.common.xa.ProposalFactory;
import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAProposal;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisMsgDispatcher implements CacheDataSubscriber,BeanFactoryAware {

	
	private static JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();
	private BeanFactory bf;
	private boolean deprecated = false;
	@Autowired
	private ProposalFactory factory;
	@Autowired
	private List<RedisTxMsgHandler> handlers;

	private CacheUtils cacheUtil ;
	
	private static final ExecutorService es = Executors.newFixedThreadPool(100);

	public RedisMsgDispatcher() throws Exception {
		cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
	}
	

	public void notify(String channel, String newMsg) {
		try {
			Map<String, Object> dataMap = jsonUtil.json2Object(newMsg, Map.class);
			String txID = getTxID(dataMap);
			TransactionPhase phase = getCurrentPhase(channel);
			XAContext context = new RedisTransactionContext(txID, phase);
			XAProposal proposal = factory.createProposal(context.getTransactionID(), context.getContextType(), context.getQuorum(), dataMap);
			
			for(RedisTxMsgHandler handler : handlers) {
				if(handler.accept(channel, proposal)) {
					if(handler.isAssignable()) {
						while(context.assign()) {
							es.submit(new RedisMsgHandlerThread(dataMap, phase, context, proposal,handler));
						}
					}
					else {
						es.submit(new RedisMsgHandlerThread(dataMap, phase, context, proposal,handler));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String getTxID(Map<String, Object> dataMap) {
		if(dataMap.containsKey(XAConstant.CONTEXT_KEYS.PROPOSAL_ID)) {
			return (String) dataMap.get(XAConstant.CONTEXT_KEYS.PROPOSAL_ID);
		}
		else if(dataMap.containsKey("id")){
			return (String) dataMap.get("id");
		}
		return null;
	}
	
	public TransactionPhase getCurrentPhase(String channel) {
		switch(channel) {
		case XAConstant.REDIS_CHANNLE_NAMES.PREPARE :
			return TransactionPhase.PREPARE;
		case XAConstant.REDIS_CHANNLE_NAMES.COMMIT:
			return TransactionPhase.COMMIT;
		case XAConstant.REDIS_CHANNLE_NAMES.ROLLBACK:
			return TransactionPhase.ROLLBACK;
		case XAConstant.REDIS_CHANNLE_NAMES.PREPARE + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE:
			return TransactionPhase.PREPARE;
		case XAConstant.REDIS_CHANNLE_NAMES.COMMIT + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE:
			return TransactionPhase.COMMIT;
		case XAConstant.REDIS_CHANNLE_NAMES.ROLLBACK + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE:
			return TransactionPhase.ROLLBACK;
		default:
			return null;
		}
		
	}
	
	public class RedisMsgHandlerThread implements Callable{
		
		private Map<String, Object> datas;
		private TransactionPhase phase;
		private XAContext context;
		private XAProposal proposal;
		private RedisTxMsgHandler handler;

		public RedisMsgHandlerThread(Map<String, Object> dataMap, TransactionPhase phase, XAContext context,
				XAProposal proposal,RedisTxMsgHandler handler) {
			super();
			this.datas = dataMap;
			this.phase = phase;
			this.context = context;
			this.proposal = proposal;
			this.handler = handler;
		}

		@Override
		public Object call() throws Exception {
			handler.handle(proposal, phase, context, datas);
			return null;
		}
		
	}


	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.bf = beanFactory;
	}


	@Override
	public void unsubscribe() {
		deprecated = true;
	}


	@Override
	public boolean isUnsubscribed() {
		return deprecated;
	}
}
