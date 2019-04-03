package org.aztec.deadsea.sql.impl;

import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.meta.SqlMetaData;

public class GenerationParam implements GenerationParameter{
	
	private SqlMetaData metaData;
	private ShardingConfiguration conf;

	public GenerationParam(SqlMetaData metaData, ShardingConfiguration conf) {
		super();
		this.metaData = metaData;
		this.conf = conf;
	}

	public SqlMetaData getSqlMetaData() {
		return metaData;
	}

	public <T> T get(String key) {
		return null;
	}


	public ShardingConfiguration getShardingConf() {
		return conf;
	}

	
}
