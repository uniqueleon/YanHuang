package org.aztec.deadsea.common;

import java.util.List;

public interface ServerRegistration {

	public List<RealServer> getAllServers();
	public ServerScaler getScaler();
	public ShardingAge getAge();
}
