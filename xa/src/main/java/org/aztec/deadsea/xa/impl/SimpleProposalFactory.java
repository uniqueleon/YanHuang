package org.aztec.deadsea.xa.impl;

import java.util.Map;

import org.aztec.deadsea.common.xa.ProposalFactory;
import org.aztec.deadsea.common.xa.XAProposal;
import org.springframework.stereotype.Component;

@Component
public class SimpleProposalFactory implements ProposalFactory {

	public SimpleProposalFactory() {
		// TODO Auto-generated constructor stub
	}

	public XAProposal createProposal(String txID, String type,int quorum,Map<String, Object> attachments) {
		// TODO Auto-generated method stub
		return new SimpleProposal(txID,type,quorum, attachments);
	}

}
