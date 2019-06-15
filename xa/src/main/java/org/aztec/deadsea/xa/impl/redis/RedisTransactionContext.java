package org.aztec.deadsea.xa.impl.redis;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.aztec.autumn.common.utils.CacheException;
import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
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
	private static List<String> allTxIds = Lists.newCopyOnWriteArrayList();
	private Map<String, Object> contextObjs = Maps.newConcurrentMap();
	private String txID;
	private Integer assignmentID;
	private Integer quorum;

	static {

		try {
			cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
			String txIds = cacheUtil.get(XAConstant.REDIS_KEY.TRANSACTIONS_ID, String.class);
			if (StringUtils.isNotEmpty(txIds)) {
				allTxIds.addAll(Lists.newArrayList(txIds.split(",")));
			}
			jsonUtil = UtilsFactory.getInstance().getJsonUtils();
		} catch (Exception e) {
			DeadSeaLogger.error("[XA]", e);
		}
	}

	public static List<String> getAllTxIDs() {
		List<String> retIDs = Lists.newArrayList();
		if (allTxIds != null) {
			retIDs.addAll(allTxIds);
		}
		return retIDs;
	}

	public RedisTransactionContext(XAProposal proposal, TransactionPhase phase) throws Exception {
		this.txID = proposal.getTxID();
		this.phase = phase;
		this.quorum = proposal.getQuorum();
		if (allTxIds.contains(proposal.getTxID())) {
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

		
		contextObjs.putAll(proposal.getContent());
		contextObjs.put(XAConstant.CONTEXT_KEYS.PHASE, phase.name());
	}

	private void addNewTransactionID(String txID) throws CacheException {
		
		StringBuilder builder = new StringBuilder();
		for (String txId : allTxIds) {
			builder.append(txId + ",");
		}
		builder.append(txID);
		cacheUtil.cache(XAConstant.REDIS_KEY.TRANSACTIONS_ID, builder.toString());
	}

	private void restoreContext() throws Exception {
		String cacheContent = cacheUtil.get(XAConstant.REDIS_KEY.TRASACTION_INFO_PREFIX + txID
				, String.class);
		contextObjs = jsonUtil.json2Object(cacheContent, Map.class);
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
			String oldIds = cacheUtil.get(XAConstant.REDIS_KEY.TRANSACTIONS_ID, String.class);
			if(!oldIds.contains(txID)) {
				addNewTransactionID(txID);
			}
			cacheUtil.cache(XAConstant.REDIS_KEY.TRASACTION_INFO_PREFIX + txID,
					jsonUtil.object2Json(contextObjs));
		} catch (Exception e) {
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

}
