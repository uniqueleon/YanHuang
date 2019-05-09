package org.aztec.deadsea.common;

import java.util.List;


public interface ServerRegister {

	public void regist(Authentication auth,List<RealServer> newServers) throws DeadSeaException;
	public void update(Authentication auth,List<RealServer> newServers) throws DeadSeaException;
	public ServerRegistration getRegistration(Authentication auth) throws DeadSeaException;
}
