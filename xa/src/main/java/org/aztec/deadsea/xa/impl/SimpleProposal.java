package org.aztec.deadsea.xa.impl;

import java.util.Map;

import org.aztec.deadsea.common.xa.XAProposal;

public class SimpleProposal implements XAProposal{
	
	private Map<String,Object> attachments;
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

}
