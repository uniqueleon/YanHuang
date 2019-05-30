package org.aztec.deadsea.xa.impl.redis;

import java.util.List;

import org.aztec.deadsea.common.xa.XACoordinator;
import org.aztec.deadsea.common.xa.XAProposal;
import org.aztec.deadsea.common.xa.XAResponse;

public class RedisTxPublisher implements XACoordinator {

	public RedisTxPublisher() {
		// TODO Auto-generated constructor stub
	}

	public List<XAResponse> prepare(XAProposal proposal) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<XAResponse> commit(XAProposal proposal) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<XAResponse> rollback(XAProposal proposal) {
		// TODO Auto-generated method stub
		return null;
	}

}
