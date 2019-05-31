package org.aztec.deadsea.xa.impl;

import java.util.Map;

import org.aztec.deadsea.common.xa.XAProposal;

public class SimpleProposal implements XAProposal{
	
	private Map<String,Object> attachments;
	private String txId;
	private int quorum;

	public SimpleProposal(String txId,
			int quorum,Map<String,Object> attachements) {
		this.txId = txId;
		this.attachments = attachements;
		this.quorum = quorum;
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

}
