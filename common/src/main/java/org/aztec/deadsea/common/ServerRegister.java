package org.aztec.deadsea.common;

import java.util.List;


public interface ServerRegister {

	public void registServer(Authentication auth,ShardingAge age,List<RealServer> newServers) throws DeadSeaException;
	public void updateServer(Authentication auth,ShardingAge age,List<RealServer> newServers) throws DeadSeaException;
	public ServerRegistration getServerRegistration(Authentication auth,ShardingAge age) throws DeadSeaException;
	
}
