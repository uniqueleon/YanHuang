package org.aztec.deadsea.common;

import java.util.List;


public interface ServerRegister {

	public void regist(Authentication auth,ShardingAge age,List<RealServer> newServers) throws DeadSeaException;
	public void update(Authentication auth,ShardingAge age,List<RealServer> newServers) throws DeadSeaException;
	public ServerRegistration getRegistration(Authentication auth,ShardingAge age) throws DeadSeaException;
}
