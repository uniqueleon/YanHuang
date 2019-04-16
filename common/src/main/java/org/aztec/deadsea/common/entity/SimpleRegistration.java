package org.aztec.deadsea.common.entity;

import java.util.List;

import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegistration;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.common.ShardingInfoCalculator;

public class SimpleRegistration implements ServerRegistration {
	
	private List<RealServer> allServers;
	private ShardingAge age;
	private ShardingInfoCalculator calculator;

	public SimpleRegistration() {
		// TODO Auto-generated constructor stub
	}

	public List<RealServer> getAllServers() {
		// TODO Auto-generated method stub
		return allServers;
	}

	public ShardingAge getLastAge() {
		return age;
	}

	public ShardingInfoCalculator getCalculator() {
		return calculator;
	}

}
