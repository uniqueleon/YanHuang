package org.aztec.deadsea.common.xa;

import java.util.List;

public interface XACoordinator {

	public void prepare(XAProposal proposal,XAPhaseListener aware) throws XAException;
	public void commit(XAProposal proposal,XAPhaseListener aware) throws XAException;
	public void rollback(XAProposal proposal,XAPhaseListener aware) throws XAException;
	public List<XAResponse> getResponse(XAProposal proposal) throws XAException;
}
