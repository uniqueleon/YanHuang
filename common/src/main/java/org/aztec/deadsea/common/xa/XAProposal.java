package org.aztec.deadsea.common.xa;

import java.util.Map;

public interface XAProposal {

	public String getTxID();
	public Map<String,Object> getAttachments();
}
