package org.aztec.deadsea.common.xa;

public interface XAContext {

	public <T> T cast();
	public Object get(String key);
	public void put(String key,Object value);
	public String getTransactionID();
	public TransactionPhase getCurrentPhase();
	public void persist() throws XAException;
	public Integer getAssignmentNo();
	public void setAssignmentNo(Integer no);
}
