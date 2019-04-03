package org.aztec.deadsea.sql.impl;

import org.aztec.deadsea.sql.ShardingConfiguration;

public class DefaultShardingConfiguration implements ShardingConfiguration {

	public DefaultShardingConfiguration() {
		// TODO Auto-generated constructor stub
	}

	public int getTableNum() {
		// TODO Auto-generated method stub
		return 101;
	}

	public int getDataBaseNum() {
		// TODO Auto-generated method stub
		return 11;
	}

	public int getVirtualServerNum() {
		return 1;
	}

	public int getRealServerNum() {
		return 1;
	}

}
