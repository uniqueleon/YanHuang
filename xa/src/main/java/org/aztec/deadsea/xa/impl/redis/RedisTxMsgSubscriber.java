package org.aztec.deadsea.xa.impl.redis;

import java.util.List;

import org.aztec.autumn.common.utils.CacheException;
import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.cache.CacheDataSubscriber;
import org.aztec.deadsea.common.DeadSeaLogger;
import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAExecutor;
import org.aztec.deadsea.common.xa.XAResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisTxMsgSubscriber implements CacheDataSubscriber {

	@Autowired
	private List<XAExecutor> executors;
	private boolean unsubscribe = false;
	private CacheUtils cacheUtil;
	private JsonUtils jsonUtil;

	public RedisTxMsgSubscriber() throws Exception {

		cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
		jsonUtil = UtilsFactory.getInstance().getJsonUtils();
	}

	@Override
	public void notify(String channel, String newMsg) {
		// TODO Auto-generated method stub
		try {
			String[] prefix = getChannelPrefix();
			int index = -1;
			for (int i = 0; i < prefix.length; i++) {
				if (channel.startsWith(prefix[i])) {
					index = i;
				}
			}
			if (index == -1) {
				return;
			}
			TransactionPhase phase = TransactionPhase.values()[index];
			String txID = channel.substring(channel.lastIndexOf("_") + 1, channel.length());
			XAContext context = new RedisTransactionContext(txID, phase);
			
			String pubChannel = null;
			Integer assignmentNo = getAssignmentNo(txID);
			while(assignmentNo != null) {
				XAResponse response = null;
				context.setAssignmentNo(assignmentNo);
				for (XAExecutor executor : executors) {
					switch (phase) {
					case PREPARE:
						response = executor.prepare(context);
						context.persist();
						pubChannel = RedisTxAckSubscriber.getSubscribeChannels(txID)[0];
						break;
					case COMMIT:
						response = executor.commit(context);
						context.persist();
						pubChannel = RedisTxAckSubscriber.getSubscribeChannels(txID)[1];
						break;
					case ROLLBACK:
						response = executor.rollback(context);
						context.persist();
						pubChannel = RedisTxAckSubscriber.getSubscribeChannels(txID)[2];
						break;
					}
				}
				String msg = jsonUtil.object2Json(response);
				cacheUtil.publish(pubChannel, msg);
				assignmentNo = getAssignmentNo(txID);
			}
		} catch (Exception e) {
			DeadSeaLogger.error("[XA]", e);
		}
	}
	
	public Integer getAssignmentNo(String txID) throws CacheException {
		cacheUtil.lock(XAConstant.REDIS_KEY.TRANSACTIONS_SEQ_NO_LOCK + txID);
		Integer no = 0;
		String seqNoStr = cacheUtil.get(XAConstant.REDIS_KEY.TRANSACTIONS_SEQ_NO + txID, String.class);
		if(seqNoStr != null) {
			no = Integer.parseInt(seqNoStr);
		}
		no ++;
		cacheUtil.cache(XAConstant.REDIS_KEY.TRANSACTIONS_SEQ_NO + txID, no);
		cacheUtil.unlock(XAConstant.REDIS_KEY.TRANSACTIONS_SEQ_NO_LOCK + txID);
		return no;
	}

	@Override
	public void unsubscribe() {
		unsubscribe = true;
	}

	@Override
	public boolean isUnsubscribed() {
		return unsubscribe;
	}

	public static String[] getChannelPrefix() {
		return new String[] { XAConstant.REDIS_CHANNLE_NAMES.PREPARE, XAConstant.REDIS_CHANNLE_NAMES.COMMIT,
				XAConstant.REDIS_CHANNLE_NAMES.ROLLBACK };
	}

}
