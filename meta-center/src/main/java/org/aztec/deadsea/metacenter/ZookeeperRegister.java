package org.aztec.deadsea.metacenter;

import java.util.List;

import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.ServerRegistration;
import org.aztec.deadsea.metacenter.conf.BaseInfo;

public class ZookeeperRegister implements ServerRegister{
	
	private static final BaseInfo baseInfo = BaseInfo.getInstance();

	public ZookeeperRegister() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void loadMetaData() {
		
	}

	public ServerRegistration regist(List<RealServer> newServers) {
		// TODO Auto-generated method stub
		//StringBuilder oriServers = new StringBuilder(realServers.getRealServers());
		return null;
	}
	
	

}
