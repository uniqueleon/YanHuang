package org.aztec.deadsea.xa.impl.redis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.cache.CacheDataSubscriber;
import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAPhaseListener;
import org.aztec.deadsea.common.xa.XAProposal;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.common.xa.XAResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

public class RedisTxAckSubscriber implements CacheDataSubscriber {
	
	private XAProposal proposal;
	private List<XAResponse> responses;
	protected boolean unsubscribed = false;
	TransactionPhase lastPhase  = null;
	private JsonUtils jsonUtil;
	@Autowired
	private List<XAPhaseListener> listeners;
	@Autowired
	private XAResponseBuilder builder;
	
	private Object lockObj  = new Object();

	public RedisTxAckSubscriber(XAProposal proposal) {
		this.proposal = proposal;
		jsonUtil = UtilsFactory.getInstance().getJsonUtils();
		ExecutorService es = Executors.newFixedThreadPool(1);
		es.submit(new PhaseNotifyThread());
	}

	public void notify(String channel, String newMsg) {
		if(lastPhase == null) {
			responses = Lists.newArrayList();
		}
		TransactionPhase currentPhase = null;
		final String[] channelNames = getSubscribeChannels(proposal.getTxID());
		
		if(channel.equals(channelNames[0])) {
			currentPhase = TransactionPhase.PREPARE;
		}
		else if (channel.startsWith(channelNames[1])) {
			currentPhase = TransactionPhase.COMMIT;
		}
		else if (channel.startsWith(channelNames[2])) {
			currentPhase = TransactionPhase.ROLLBACK;
		}
		if(!currentPhase.equals(lastPhase)) {
			lastPhase = currentPhase;
			responses.clear();
		}
		else {
			try {
				Map<String,Object> dataMap = jsonUtil.json2Object(newMsg, Map.class);
				XAResponse response = builder.buildSuccess(proposal.getTxID(), (Integer)dataMap.get("seqNo"),currentPhase);
				responses.add(response);
				lockObj.notifyAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<XAResponse> getResponses(){
		return responses;
	}

	public void unsubscribe() {
		responses.clear();
		unsubscribed = true;
	}

	public boolean isUnsubscribed() {
		return unsubscribed;
	}
	
	public static String[] getSubscribeChannels(String txID) {
		return new String[] {
				XAConstant.REDIS_CHANNLE_NAMES.PREPARE + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE
						+ XAConstant.REDIS_CHANNLE_NAMES.SEPERATOR + txID,
				XAConstant.REDIS_CHANNLE_NAMES.COMMIT + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE
						+ XAConstant.REDIS_CHANNLE_NAMES.SEPERATOR + txID,
				XAConstant.REDIS_CHANNLE_NAMES.ROLLBACK + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE
						+ XAConstant.REDIS_CHANNLE_NAMES.SEPERATOR + txID,

		};
	}
	
	public class PhaseNotifyThread implements Callable {

		private boolean runnable = true;
		@Override
		public Object call() throws Exception {
		
			while(runnable) {
				lockObj.wait();
				for(XAPhaseListener listener : listeners) {
					listener.listen(responses);
				}
			}
			return null;
		}
		
	}
	

}
