package org.aztec.deadsea.xa.impl;

import java.util.List;

import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.common.xa.XAResponseSet;

import com.sun.research.ws.wadl.Representation;

public class SimpleXAResponseSet implements XAResponseSet {
	
	private TransactionPhase phase;
	private String txID;
	private List<XAResponse> responses;
	private int quorum;
	private boolean pass = false;

	public SimpleXAResponseSet() {
	}
	
	

	public SimpleXAResponseSet(TransactionPhase phase, String txID, List<XAResponse> responses, int quorum) {
		super();
		this.phase = phase;
		this.txID = txID;
		this.responses = responses;
		this.quorum = quorum;
		int successCount = 0;
		for(XAResponse response : responses) {
			if(response.isOK()) {
				successCount ++;
			}
		}
		pass = (successCount >= quorum);
	}



	@Override
	public TransactionPhase getCurrentPhase() {
		return phase;
	}

	@Override
	public String getTxID() {
		return txID;
	}

	@Override
	public List<XAResponse> getLastResponses() {
		return responses;
	}

	@Override
	public boolean isPassed() {
		return pass;
	}

	@Override
	public int getQuorum() {
		return quorum;
	}

}
