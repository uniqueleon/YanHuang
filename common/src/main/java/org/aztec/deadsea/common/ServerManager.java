package org.aztec.deadsea.common;

import java.util.List;

public interface ServerManager {

	public ShardingInfoCalculator getCalculator();
	public List<Server> reorganize();
	public void publish(List<Server> serverInfos);
}
