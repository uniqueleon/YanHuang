package org.aztec.deadsea.metacenter;

public interface MetaCenterConst {

	public interface ZkConfigPaths{
		public static String SERVER_BASE_INFO = "com.aztec.deadsea.meta";
		public static String REAL_SERVER_AGE_INFO =  "com.aztec.deadsea.meta.realserver.ages.%s";
		public static String REAL_SERVER_INFO = REAL_SERVER_AGE_INFO + ".%s";
		public static String BASE_INFO = "com.aztec.deadsea.meta.base";
		public static String BASE_AUTHENTICATIONS_INFO = "com.aztec.deadsea.meta.auth.%s";
		public static String DATABASE_INFO = "com.aztec.deadsea.meta.%s.db.%s";
		public static String TABLES_SHARDING_INFO = DATABASE_INFO + ".tables.%s";
		public static String SHARDING_AGE_INFO = TABLES_SHARDING_INFO + ".ages.%s";
	}
	
}
