package org.aztec.deadsea.xa.impl.redis;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aztec.autumn.common.utils.CacheException;
import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.NetworkUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.cache.CacheDataSubscriber;
import org.aztec.deadsea.common.DeadSeaLogger;
import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAProposal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RedisTransactionContext implements XAContext {

	private static CacheUtils cacheUtil;
	private static JsonUtils jsonUtil;
	private TransactionPhase phase;
	private static Map<String, String> allTx = Maps.newConcurrentMap();
	private Map<String, Object> contextObjs = Maps.newConcurrentMap();
	private static final Map<String, Map<String, Object>> localMaps = Maps.newConcurrentMap();
	private String txID;
	private Integer assignmentID;
	private Integer quorum;
	private String type;
	private static RedisTransactionSubscriber msgSubscriber = new RedisTransactionSubscriber();

	static {

		try {
			jsonUtil = UtilsFactory.getInstance().getJsonUtils();
			cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
			allTx = cacheUtil.hgetAll(XAConstant.REDIS_KEY.ALL_TRANSACTIONS);
			cacheUtil.subscribe(msgSubscriber, XAConstant.REDIS_TX_CHANNELS.TRANSACTIONS_UPDATE_CHANNEL);
		} catch (Exception e) {
			DeadSeaLogger.error("[XA]", e);
		}
	}

	public static List<String> getAllTxIDs() {
		List<String> retIDs = Lists.newArrayList();
		if (allTx != null) {
			retIDs.addAll(allTx.keySet());
		}
		return retIDs;
	}

	public RedisTransactionContext(XAProposal proposal, TransactionPhase phase) throws Exception {
		this.txID = proposal.getTxID();
		this.type = proposal.getType();
		this.phase = phase;
		this.quorum = proposal.getQuorum();
		if (allTx.containsKey(proposal.getTxID())) {
			restoreContext();
		} else {
			newContext(proposal, phase);
		}
	}

	public RedisTransactionContext(String id, TransactionPhase phase) throws Exception {
		this.txID = id;
		this.phase = phase;
		restoreContext();
	}

	private void newContext(XAProposal proposal, TransactionPhase phase) throws Exception {

		Map<String, Object> newLocalMap = Maps.newConcurrentMap();
		for (Entry<String, Object> content : proposal.getContent().entrySet()) {
			if (content.getKey().startsWith(XAConstant.CONTEXT_LOCAL_KEYS.LOCAL_CONTEXT_PERFIX)) {
				newLocalMap.put(content.getKey(), content.getValue());
			} else {
				contextObjs.put(content.getKey(), content.getValue());
			}
		}
		localMaps.put(txID, newLocalMap);
		contextObjs.put(XAConstant.CONTEXT_KEYS.PROPOSAL_ID, txID);
		contextObjs.put(XAConstant.CONTEXT_KEYS.QUORUM, quorum);
		contextObjs.put(XAConstant.CONTEXT_KEYS.PHASE, phase.name());
		contextObjs.put(XAConstant.CONTEXT_KEYS.TYPE, proposal.getType());
	}

	private String toPersistContent() throws Exception {
		return jsonUtil.object2Json(contextObjs);
	}

	private void restoreContext() throws Exception {
		String cacheContent = allTx.get(txID);
		contextObjs = jsonUtil.json2Object(cacheContent, Map.class);
		this.type = (String) contextObjs.get(XAConstant.CONTEXT_KEYS.TYPE);
		this.quorum = (Integer) contextObjs.get(XAConstant.CONTEXT_KEYS.QUORUM);
		this.assignmentID = (Integer) contextObjs.get(XAConstant.CONTEXT_KEYS.TX_SEQUENCE_NO);
	}

	@Override
	public <T> T cast() {
		return (T) this;
	}

	@Override
	public Object get(String key) {
		return contextObjs.get(key);
	}

	@Override
	public void put(String key, Object value) {
		contextObjs.put(key, value);
	}

	@Override
	public String getTransactionID() {
		return txID;
	}

	@Override
	public TransactionPhase getCurrentPhase() {
		return phase;
	}

	@Override
	public void persist() throws XAException {
		try {
			updateTx(txID, true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new XAException();
		}
	}

	@Override
	public Integer getAssignmentNo() {
		return assignmentID;
	}

	@Override
	public void setAssignmentNo(Integer id) {
		this.assignmentID = id;
	}

	@Override
	public Integer getQuorum() {
		return quorum;
	}

	@Override
	public void setCurrentPhase(TransactionPhase phase) {
		this.phase = phase;
	}

	@Override
	public String getContextType() {
		return type;
	}

	@Override
	public Object getLocal(String key) {
		Map<String, Object> dataMap = getTxLocalData();
		return dataMap == null ? null : dataMap.get(key);
	}

	public Map<String, Object> getTxLocalData() {
		return localMaps.get(txID);
	}

	@Override
	public void setLocal(String key, Object value) {
		Map<String, Object> dataMap = getTxLocalData();
		dataMap.put(key, value);
	}

	@Override
	public void removeLocal(String key) {
		Map<String, Object> dataMap = getTxLocalData();
		dataMap.remove(key);
	}

	public void updateTx(String curId, boolean append) throws CacheException {
		try {
			if (append) {
				String content = toPersistContent();
				String localhost = NetworkUtils.getLocalHost();
				if (!allTx.containsKey(curId)) {
					allTx.put(curId, content);
				}
				else {
					try {
						validate(localhost);
					} catch (Exception e) {
						restoreContext();
						throw e;
					}
				}
				cacheUtil.lock(XAConstant.REDIS_LOCKS.TRANSACTION_LOCK + txID, XAConstant.REDIS_LOCKS.DEFAULT_LOCK_TIMEOUT);
				if(assignmentID != null) {
					registTxServer(localhost);
				}
				cacheUtil.hset(XAConstant.REDIS_KEY.ALL_TRANSACTIONS, curId, content);
			} else {
				cacheUtil.hdel(XAConstant.REDIS_KEY.ALL_TRANSACTIONS, curId);
			}
			cacheUtil.publish(XAConstant.REDIS_TX_CHANNELS.TRANSACTIONS_UPDATE_CHANNEL, getNotifyMsg(curId, append));
		} catch (Exception e) {
			throw new CacheException(e.getMessage(), e);
		} finally {
			cacheUtil.unlock(XAConstant.REDIS_LOCKS.TRANSACTIONS_LOCK);
			cacheUtil.unlock(XAConstant.REDIS_LOCKS.TRANSACTION_LOCK + txID);
		}
	}
	
	public String getTxServerKey() {
		return XAConstant.REDIS_KEY.TRANSACTIONS_SERVERS + txID;
	}
	
	public void validate(String localhost) throws Exception {
		String remoteContent = cacheUtil.hget(XAConstant.REDIS_KEY.ALL_TRANSACTIONS, txID);
		boolean conflict = false;
		Map<String,Object> remoteData = jsonUtil.json2Object(remoteContent, Map.class);
		Integer remoteAssignment = (Integer) remoteData.get(XAConstant.CONTEXT_KEYS.TX_SEQUENCE_NO);
		if(remoteAssignment != null && remoteAssignment >= this.assignmentID) {
			List<String> serverList = cacheUtil.listAll(getTxServerKey());
			if(serverList.size() > assignmentID && !serverList.get(assignmentID).equals(localhost)) {
				conflict = true;
			}
		}
		if(conflict) {
			throw new CacheException(" CONTEXT CONFLICT WITH REMOTE SERVER!");
		}
		
	}
	
	public void registTxServer(String localhost) throws CacheException {
		if(assignmentID == null) {
			return ;
		}
		String redisKey = getTxServerKey();
		if(cacheUtil.exists(redisKey)) {
			cacheUtil.lset(redisKey,assignmentID, localhost);
		}else {
			cacheUtil.lpush(redisKey, localhost);
		}
	}
	
	public String getNotifyMsg(String curId,boolean append) {
		return (append ? XAConstant.REDIS_TX_CHANNELS.UPDATE_SIGNAL_ADD : 
			 XAConstant.REDIS_TX_CHANNELS.UPDATE_SIGNAL_REMOVE) + "_" + curId;
	}

	@Override
	public void destroy() {
		try {
			updateTx(txID, false);
		} catch (CacheException e) {
			DeadSeaLogger.error(XAConstant.LOG_KEY, e);
		}
	}

	public static class RedisTransactionSubscriber implements CacheDataSubscriber {

		private boolean subscribed = true;

		@Override
		public void notify(String channel, String newMsg) {
			try {
				reloadTx(newMsg);
			} catch (CacheException e) {
				DeadSeaLogger.error(XAConstant.LOG_KEY, e);
			}
		}

		public void reloadTx(String msg) throws CacheException {
			String[] msgArr = msg.split("_");
			String curId = msgArr[1];
			synchronized (allTx) {
				switch(msgArr[0]){
				case XAConstant.REDIS_TX_CHANNELS.UPDATE_SIGNAL_ADD:
					allTx.put(curId, cacheUtil.hget(XAConstant.REDIS_TX_CHANNELS.TRANSACTIONS_UPDATE_CHANNEL,curId));
				case XAConstant.REDIS_TX_CHANNELS.UPDATE_SIGNAL_REMOVE:
					allTx.remove(curId);
				}
			}
		}

		@Override
		public void unsubscribe() {
			subscribed = false;
		}

		@Override
		public boolean isUnsubscribed() {
			return subscribed;
		}

	}

	@Override
	public boolean assign()  {
		try {
			Integer assignmentNo = -1;
			if(contextObjs.containsKey(XAConstant.CONTEXT_KEYS.TX_SEQUENCE_NO)) {
				assignmentNo = (Integer) contextObjs.get(XAConstant.CONTEXT_KEYS.TX_SEQUENCE_NO);
			}
			assignmentNo ++;
			if(assignmentNo >= quorum) {
				return false;
			}
			this.assignmentID  = assignmentNo;
			contextObjs.put(XAConstant.CONTEXT_KEYS.TX_SEQUENCE_NO, assignmentID);

			persist();
			return true;
		} catch (Exception e) {
			DeadSeaLogger.error(XAConstant.LOG_KEY, e);
			return false;
		}
	}


}
