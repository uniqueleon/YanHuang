package org.aztec.deadsea.common.entity;

import java.util.List;

import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegistration;
import org.aztec.deadsea.common.ServerScaler;
import org.aztec.deadsea.common.ShardingAge;

public class SimpleRegistration implements ServerRegistration {
	
	private List<RealServer> allServers;
	private ServerScaler calculator;

	public SimpleRegistration() {
		// TODO Auto-generated constructor stub
	}
	
	

	public void setAllServers(List<RealServer> allServers) {
		this.allServers = allServers;
	}



	public void setCalculator(ServerScaler calculator) {
		this.calculator = calculator;
	}



	public List<RealServer> getAllServers() {
		// TODO Auto-generated method stub
		return allServers;
	}

	public ServerScaler getScaler() {
		return calculator;
	}



	@Override
	public ShardingAge getAge() {
		// TODO Auto-generated method stub
		return null;
	}

}
