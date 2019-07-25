package org.aztec.deadsea.common.xa;

import java.util.Map;

public interface ProposalFactory {

	public XAProposal createProposal(String txID,String type,int quorum,Map<String,Object> attachments)  throws XAException;
	public XAProposal rebuild(XAContext context) throws XAException;
}
