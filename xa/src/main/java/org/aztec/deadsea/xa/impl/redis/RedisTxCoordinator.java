package org.aztec.deadsea.xa.impl.redis;

import java.util.concurrent.atomic.AtomicBoolean;

import org.aztec.autumn.common.utils.CacheException;
import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.cache.CacheDataSubscriber;
import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XACoordinator;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAPhaseListener;
import org.aztec.deadsea.common.xa.XAProposal;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisTxCoordinator implements XACoordinator, BeanFactoryAware {

	@Autowired
	CacheDataSubscriber subscriber;
	private BeanFactory bf;
	private CacheUtils cacheUtil;
	private static final AtomicBoolean inited = new AtomicBoolean(false);

	public RedisTxCoordinator() throws Exception {
		// TODO Auto-generated constructor stub
		cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
	}
	
	private void init() throws CacheException {
		if (!inited.get()) {
			synchronized (inited) {
				if(subscriber != null) {
					cacheUtil.subscribe(subscriber,XAConstant.DEFAULT_REDIS_PUBLISH_CHANNELS);
					inited.set(true);
				}
			}
		}
	}

	public void prepare(XAProposal proposal, XAPhaseListener aware) throws XAException {
		try {
			init();
			cacheUtil.cache(XAConstant.REDIS_KEY.TRANSACTIONS_SEQ_LIMIT + proposal.getTxID(), "" + proposal.getQuorum());
			RedisTransactionContext context = new RedisTransactionContext(proposal, TransactionPhase.PREPARE);
			context.persist();
			cacheUtil.publish(XAConstant.DEFAULT_REDIS_PUBLISH_CHANNELS[0], proposal.toJson());
		} catch (Exception e) {
			throw new XAException();
		}
	}


	public void commit(XAProposal proposal) throws XAException {
		try {
			init();
			cacheUtil.remove(XAConstant.REDIS_KEY.TRANSACTIONS_SEQ_NO + proposal.getTxID());
			cacheUtil.publish(XAConstant.DEFAULT_REDIS_PUBLISH_CHANNELS[1], proposal.toJson());
			//cleanUp(proposal);
		} catch (Exception e) {
			throw new XAException();
		}
	}
	

	public void rollback(XAProposal proposal) throws XAException {
		try {
			init();
			cacheUtil.remove(XAConstant.REDIS_KEY.TRANSACTIONS_SEQ_NO + proposal.getTxID());
			cacheUtil.publish(XAConstant.DEFAULT_REDIS_PUBLISH_CHANNELS[2], proposal.toJson());
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
	public void finished(XAProposal proposal) throws XAException {
		// TODO Auto-generated method stub
	}

}
