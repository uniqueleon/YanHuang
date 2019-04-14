package org.aztec.deadsea.common;

import java.util.List;

public interface ServerManager {

	public ShardingInfoCalculator getCalculator();
	public List<RealServer> reorganize();
	public void publish(List<RealServer> serverInfos);
}
