package org.aztec.deadsea.common;

import java.util.List;

public interface ServerRegistration {

	public List<RealServer> getAllServers();
	public int currentServerSize();
	public int nextServerSize();
	public ShardingAge getLastAge();
}
