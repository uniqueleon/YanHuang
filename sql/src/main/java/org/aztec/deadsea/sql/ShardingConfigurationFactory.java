package org.aztec.deadsea.sql;

import org.aztec.deadsea.sql.meta.SqlMetaData;

public interface ShardingConfigurationFactory {

	public ShardingConfiguration getConfiguration(SqlMetaData metaData);
}
