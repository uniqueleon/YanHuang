package org.aztec.deadsea.common;

import java.util.List;

public interface ServerManager {

	public ServerScaler getCalculator();
	public List<RealServer> reorganize();
	public void publish(List<RealServer> serverInfos);
}
