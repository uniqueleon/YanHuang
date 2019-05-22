package org.aztec.deadsea.sql;

import org.aztec.deadsea.sql.meta.SqlMetaData;

public interface ShardingConfigurationFactory {


	public static final String DEFAULT_CONFIGURATION_BEAN_NAME = "defaultConfiguration";
	public ShardingConfiguration getConfiguration() throws ShardingSqlException;
}
