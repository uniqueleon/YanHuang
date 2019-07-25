package org.aztec.deadsea.xa.impl.redis;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	private static final Map<String,Map<String, Object>> localMaps = Maps.newConcurrentMap();
	private String txID;
	private Integer assignmentID;
	private Integer quorum;
	private String type;

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
		this.type = proposal.getType();
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
		
		Map<String,Object> newLocalMap = Maps.newConcurrentMap();
		
		for(Entry<String,Object> content : proposal.getContent().entrySet()) {
			if(content.getKey().startsWith(XAConstant.CONTEXT_LOCAL_KEYS.LOCAL_CONTEXT_PERFIX)) {
				newLocalMap.put(content.getKey(),content.getValue());
			}
			else {
				contextObjs.put(content.getKey(), content.getValue());
			}
		}
		contextObjs.put(XAConstant.CONTEXT_KEYS.PROPOSAL_ID, txID);
		contextObjs.put(XAConstant.CONTEXT_KEYS.QUORUM, quorum);
		contextObjs.put(XAConstant.CONTEXT_KEYS.PHASE, phase.name());
		contextObjs.put(XAConstant.CONTEXT_KEYS.TYPE, proposal.getType());
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
		this.type = (String) contextObjs.get(XAConstant.CONTEXT_KEYS.TYPE);
		this.quorum = (Integer) contextObjs.get(XAConstant.CONTEXT_KEYS.QUORUM);
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
			if(oldIds == null || !oldIds.contains(txID)) {
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
		Map<String,Object> dataMap = getTxLocalData();
		return dataMap == null ? null : dataMap.get(key);
	}
	
	public Map<String,Object> getTxLocalData(){
		return localMaps.get(txID);
	}
	
	@Override
	public void setLocal(String key, Object value) {
		Map<String,Object> dataMap = getTxLocalData();
		dataMap.put(key, value);
	}

	@Override
	public void removeLocal(String key) {
		Map<String,Object> dataMap = getTxLocalData();
		dataMap.remove(key);
	}
	
	

}
