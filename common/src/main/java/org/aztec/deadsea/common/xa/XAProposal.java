package org.aztec.deadsea.common.xa;

import java.util.Map;

public interface XAProposal {

	public String getTxID();
	public String getType();
	public Map<String,Object> getContent();
	public String toJson() throws Exception ;
	public int getQuorum();
}
