package org.aztec.deadsea.common.xa;

import java.util.List;

public interface XACoordinator {

	public List<XAResponse> prepare(XAProposal proposal);
	public List<XAResponse> commit(XAProposal proposal);
	public List<XAResponse> rollback(XAProposal proposal);
}
