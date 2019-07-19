package org.aztec.deadsea.common.xa;

public interface XAConstant {

	public static interface REDIS_CHANNLE_NAMES{

		public static final String SEPERATOR = "_";
		public static final String PREFIX = "XA_REDIS_CHANNEL_";
		public static final String PREPARE = PREFIX + "PREPARE";
		public static final String COMMIT = PREFIX + "COMMIT";
		public static final String ROLLBACK = PREFIX + "ROLLBACK";
		public static final String ACKOWNLEDGE = "ACK";
	}
	
	public static interface REDIS_KEY {
		public static final String TRASACTION_INFO_PREFIX = "XA_REDIS_TX_";
		public static final String TRANSACTIONS_ID = "XA_REDIS_TX_IDS";
		public static final String TRANSACTIONS_SEQ_NO_LOCK = "XA_REDIS_TX_SEQ_LOCK_";
		public static final String TRANSACTIONS_SEQ_NO = "XA_REDIS_TX_SEQ_NO_";
		public static final String TRANSACTIONS_SEQ_LIMIT = "XA_REDIS_TX_SEQ_NO_LIMIT";
	}
	
	
	public static interface CONTEXT_LOCAL_KEYS{
		public static final String LOCAL_CONTEXT_PERFIX = "LOCAL###";
		public static final String GENENRATION_PARAMS = LOCAL_CONTEXT_PERFIX + "gen_params";
		public static final String SHARDING_CONFIGURATION = LOCAL_CONTEXT_PERFIX + "shardConf";
	}
	
	public static interface CONTEXT_KEYS{
		public static final String TYPE = "type";
		public static final String PHASE = "phase";
		public static final String CONNECT_ARGS = "CONNECT_ARGS";
		public static final String EXECUTE_SQL = "TARGET_SQL";
		public static final String ROLLBACK_SQL = "ROLLBACK_SQL";
		public static final String RAW_SQLS = "RAW_SQL";
		public static final String RAW_SQL_TYPE = "RAW_SQL_TYPE";
		public static interface META_CENTER_KEYS{
			public static final String AUTH_USER_NAME = "META_CENTER_AUTH_USERNAME";
			public static final String AUTH_PASSWORD = "META_CENTER_AUTH_PWD";
			
		}
	}
	
	public static interface MSG_BASE_RESPONSE{
		public static final String OK = "OK";
		public static final String FAIL = "FAIL";
	}
	
	public static interface XA_PROPOSAL_TYPES {
		public static final String CREATE_SQL = "XA_CREATE_SQL_EXECUTOR";
	}
}
