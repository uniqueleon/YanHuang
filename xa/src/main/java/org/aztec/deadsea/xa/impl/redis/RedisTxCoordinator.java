package org.aztec.deadsea.xa.impl.redis;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.cache.CacheDataSubscriber;
import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XACoordinator;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAPhaseListener;
import org.aztec.deadsea.common.xa.XAProposal;
import org.aztec.deadsea.common.xa.XAResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;

@Component
public class RedisTxCoordinator implements XACoordinator, BeanFactoryAware {

	private BeanFactory bf;
	private CacheUtils cacheUtil;
	private JsonUtils jsonUtil;
	private static final Map<String, List<CacheDataSubscriber>> subscribers = Maps.newConcurrentMap();

	public RedisTxCoordinator() throws Exception {
		// TODO Auto-generated constructor stub
		cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
		jsonUtil = UtilsFactory.getInstance().getJsonUtils();
	}

	public void prepare(XAProposal proposal, XAPhaseListener aware) throws XAException {
		try {
			System.out.println("Doing prepare!");
			String[] channelNames = getTxChannelNames(proposal.getTxID());
			RedisTxMsgSubscriber execSub = bf.getBean(RedisTxMsgSubscriber.class);
			cacheUtil.cache(XAConstant.REDIS_KEY.TRANSACTIONS_SEQ_LIMIT + proposal.getTxID(), "" + proposal.getQuorum());
			cacheUtil.subscribe(execSub,channelNames);
			RedisTxAckSubscriber subscriber = bf.getBean(RedisTxAckSubscriber.class);
			subscriber.init(proposal);
			cacheUtil.subscribe(subscriber,RedisTxAckSubscriber.getSubscribeChannels(proposal.getTxID()));
			List<CacheDataSubscriber> subscriberList = Lists.newArrayList();
			subscriberList.add(execSub);
			subscriberList.add(subscriber);
			subscribers.put(proposal.getTxID(), subscriberList);
			RedisTransactionContext context = new RedisTransactionContext(proposal, TransactionPhase.PREPARE);
			context.persist();
			cacheUtil.publish(channelNames[0], jsonUtil.object2Json(proposal.getContent()));
		} catch (Exception e) {
			throw new XAException();
		}
	}

	private String[] getTxChannelNames(String txID) {
		return new String[] { XAConstant.REDIS_CHANNLE_NAMES.PREPARE + "_" + txID,
				XAConstant.REDIS_CHANNLE_NAMES.COMMIT + "_" + txID,
				XAConstant.REDIS_CHANNLE_NAMES.ROLLBACK + "_" + txID, };
	}

	public void commit(XAProposal proposal) throws XAException {
		try {
			System.out.println("Doing commit!");
			String[] channelNames = getTxChannelNames(proposal.getTxID());
			cacheUtil.remove(XAConstant.REDIS_KEY.TRANSACTIONS_SEQ_NO + proposal.getTxID());
			cacheUtil.publish(channelNames[1], jsonUtil.object2Json(proposal.getContent()));
			//cleanUp(proposal);
		} catch (Exception e) {
			throw new XAException();
		}
	}
	
	public void cleanUp(XAProposal proposal) {
		List<CacheDataSubscriber> subs = subscribers.get(proposal.getTxID());
		for(CacheDataSubscriber sub : subs) {
			sub.unsubscribe();
		}
		subscribers.remove(proposal.getTxID());
	}

	public void rollback(XAProposal proposal) throws XAException {
		try {
			System.out.println("Doing rollback!");
			String[] channelNames = getTxChannelNames(proposal.getTxID());
			cacheUtil.remove(XAConstant.REDIS_KEY.TRANSACTIONS_SEQ_NO + proposal.getTxID());
			cacheUtil.publish(channelNames[2], jsonUtil.object2Json(proposal.getContent()));
			//cleanUp(proposal);
		} catch (Exception e) {
			throw new XAException();
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		bf = beanFactory;
	}

	@Override
	public List<XAResponse> getResponse(XAProposal proposal) throws XAException {
		RedisTxAckSubscriber sub = (RedisTxAckSubscriber) subscribers.get(proposal.getTxID()).get(1);
		return sub.getResponses();
	}

}
