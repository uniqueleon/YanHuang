package org.aztec.deadsea.xa.impl;

import java.util.Map;

import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAProposal;

import com.google.common.collect.Maps;

public class SimpleProposal implements XAProposal{
	
	private Map<String,Object> attachments;
	private JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();
	private String txId;
	private int quorum;
	private String type;


	public SimpleProposal(String txId,String type,
			int quorum,Map<String,Object> attachements) {
		this.txId = txId;
		this.attachments = attachements;
		this.quorum = quorum;
		this.type = type;
	}

	public String getTxID() {
		return txId;
	}

	public Map<String, Object> getContent() {
		return attachments;
	}
	
	public Map<String,Object> getJSONContent(){
		Map<String,Object> retMap = Maps.newConcurrentMap();
		attachments.entrySet().stream().forEach(e -> {
			if(!e.getKey().startsWith(XAConstant.CONTEXT_LOCAL_KEYS.LOCAL_CONTEXT_PERFIX)) {
				retMap.put(e.getKey(), e.getValue());
			}
		});
		retMap.put(XAConstant.CONTEXT_KEYS.PROPOSAL_ID, txId);
		retMap.put(XAConstant.CONTEXT_KEYS.QUORUM, quorum);
		retMap.put(XAConstant.CONTEXT_KEYS.TYPE, type);
		return retMap;
	}

	public int getQuorum() {
		return quorum;
	}

	@Override
	public String getType() {
		return type;
	}

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toJson() throws Exception {
		return jsonUtil.object2Json(getJSONContent());
	}

}
