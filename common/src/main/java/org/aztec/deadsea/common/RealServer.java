package org.aztec.deadsea.common;

import java.util.List;

public interface RealServer {

	public List<VirtualServer> getNodes();
	public String getHost();
	public Integer getPort();
	
}
