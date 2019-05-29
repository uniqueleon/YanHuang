package org.aztec.deadsea.common.xa;

public interface XAResponse {

	public TransactionPhase getCurrentPhase();
	public String getID();
	public String getHost();
	public boolean isAccepted();
	public boolean isSuccess();
	public boolean isCommited();
}
