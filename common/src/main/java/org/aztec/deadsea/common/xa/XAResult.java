package org.aztec.deadsea.common.xa;

public interface XAResult {

	public XAProposal getProposal();
	public boolean isAllSuccess();
	public int successCount();
	public int failCount();
	
}
