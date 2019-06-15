package org.aztec.deadsea.xa.impl.redis;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
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

import com.google.common.collect.Maps;

@Component
public class RedisTxCoordinator implements XACoordinator, BeanFactoryAware {

	private BeanFactory bf;
	private CacheUtils cacheUtil;
	private JsonUtils jsonUtil;
	private static final Map<String, RedisTxAckSubscriber> subscribers = Maps.newConcurrentMap();

	public RedisTxCoordinator() throws Exception {
		// TODO Auto-generated constructor stub
		cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
		jsonUtil = UtilsFactory.getInstance().getJsonUtils();
	}

	public void prepare(XAProposal proposal, XAPhaseListener aware) throws XAException {
		try {
			String[] channelNames = getTxChannelNames(proposal.getTxID());
			RedisTxMsgSubscriber execSub = bf.getBean(RedisTxMsgSubscriber.class);
			cacheUtil.subscribe(execSub, channelNames);
			RedisTxAckSubscriber subscriber = bf.getBean(RedisTxAckSubscriber.class, new Object[] { proposal });
			cacheUtil.subscribe(subscriber, RedisTxAckSubscriber.getSubscribeChannels(proposal.getTxID()));
			subscribers.put(proposal.getTxID(), subscriber);
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
			String[] channelNames = getTxChannelNames(proposal.getTxID());
			cacheUtil.publish(channelNames[1], jsonUtil.object2Json(proposal.getContent()));
		} catch (Exception e) {
			throw new XAException();
		}
	}

	public void rollback(XAProposal proposal) throws XAException {
		try {
			String[] channelNames = getTxChannelNames(proposal.getTxID());
			cacheUtil.publish(channelNames[2], jsonUtil.object2Json(proposal.getContent()));
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
		RedisTxAckSubscriber sub = subscribers.get(proposal.getTxID());
		return sub.getResponses();
	}

}
