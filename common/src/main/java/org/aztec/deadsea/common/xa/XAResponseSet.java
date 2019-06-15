package org.aztec.deadsea.common.xa;

import java.util.List;

public interface XAResponseSet {

	public TransactionPhase getCurrentPhase();
	public String getTxID();
	public List<XAResponse> getLastResponses();
	public boolean isPassed();
	public int getQuorum();
}
