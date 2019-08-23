package org.aztec.deadsea.sql;

public interface ShardingSqlConstants {

	
	public static interface DISTRIBUTE_LOCKS{
		public static final String INSERT_LOCK = "";
	}
	
	public static final int SEQUENCE_NO_AQUIRED_BATCH = 1000;
	public static final String LOG_PREFIX = "[SHARD_SQL]:";
}
