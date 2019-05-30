package org.aztec.deadsea.common.xa;

import java.util.Map;

public interface ProposalFactory {

	public XAProposal createProposal(String txID,Map<String,Object> attachments);
}
