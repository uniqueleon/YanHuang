package org.aztec.deadsea.xa.impl;

import java.util.Map;

import org.aztec.deadsea.common.xa.ProposalFactory;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAProposal;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class SimpleProposalFactory implements ProposalFactory {

	private static final Map<String,XAProposal> proposals = Maps.newConcurrentMap();

	public SimpleProposalFactory() {
		// TODO Auto-generated constructor stub
	}

	public XAProposal createProposal(String txID, String type,int quorum,Map<String, Object> attachments) {
		if(proposals.containsKey(txID)) {
			return proposals.get(txID);
		}
		else {
			XAProposal proposal =  new SimpleProposal(txID,type,quorum, attachments);
			proposals.put(txID, proposal);
			return proposal;
		}
	}

	@Override
	public XAProposal rebuild(XAContext context) throws XAException {
		
		return null;
	}

}
