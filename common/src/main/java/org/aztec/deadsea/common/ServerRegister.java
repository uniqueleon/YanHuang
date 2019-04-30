package org.aztec.deadsea.common;

import java.util.List;


public interface ServerRegister {

	public void regist(List<RealServer> newServers) throws DeadSeaException;
	public void update(List<RealServer> newServers) throws DeadSeaException;
	public ServerRegistration getRegistration() throws DeadSeaException;
}
