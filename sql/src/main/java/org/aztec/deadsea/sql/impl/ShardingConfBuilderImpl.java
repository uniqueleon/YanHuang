package org.aztec.deadsea.sql.impl;

import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.springframework.stereotype.Component;

@Component
public class ShardingConfBuilderImpl implements ShardingConfigurationFactory {
	

	public ShardingConfBuilderImpl() {
		// TODO Auto-generated constructor stub
	}

	public ShardingConfiguration getConfiguration(SqlMetaData metaData) throws ShardingSqlException {
		try {
			return new LocalShardingConfiguration();
		} catch (Exception e) {
			throw new ShardingSqlException(ShardingSqlException.ErrorCodes.SHARDING_CONFIGURATION_UNAVAILABLE);
		}
	}


}
