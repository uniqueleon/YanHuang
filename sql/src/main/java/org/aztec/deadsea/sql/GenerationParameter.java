package org.aztec.deadsea.sql;

import org.aztec.deadsea.sql.meta.SqlMetaData;

public interface GenerationParameter {

	public SqlMetaData getSqlMetaData();
	public <T> T get(String key);
	public ShardingConfiguration getShardingConf();
}
