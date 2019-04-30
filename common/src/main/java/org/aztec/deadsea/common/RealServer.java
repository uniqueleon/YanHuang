package org.aztec.deadsea.common;

import java.util.List;

public interface RealServer {

	public List<VirtualServer> getNodes();
	public String getHost();
	public Integer getPort();
	public Integer getProxyPort();
	public Integer getNo();
	public boolean isNew();
	public void setNodes(List<VirtualServer> virtualNodes);
	public RealServer cloneThis();
}
