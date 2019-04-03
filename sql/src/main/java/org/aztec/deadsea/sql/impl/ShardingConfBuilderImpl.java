package org.aztec.deadsea.sql.impl;

import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.springframework.stereotype.Component;

@Component
public class ShardingConfBuilderImpl implements ShardingConfigurationFactory {

	public ShardingConfBuilderImpl() {
		// TODO Auto-generated constructor stub
	}

	public ShardingConfiguration getConfiguration(SqlMetaData metaData) {
		return new DefaultShardingConfiguration();
	}

}
