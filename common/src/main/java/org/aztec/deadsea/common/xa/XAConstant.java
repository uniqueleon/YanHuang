package org.aztec.deadsea.common.xa;

public interface XAConstant {

	public static interface REDIS_CHANNLE_NAMES{

		public static final String SEPERATOR = "_";
		public static final String PREFIX = "XA_REDIS_CHANNEL_";
		public static final String PREPARE = PREFIX + "PREPARE";
		public static final String COMMIT = PREFIX + "COMMIT";
		public static final String ROLLBACK = PREFIX + "ROLLBACK";
		public static final String ACKOWNLEDGE = "_ACK";
	}
	
	public static interface REDIS_TX_CHANNELS {
		public static final String TRANSACTIONS_UPDATE_CHANNEL = "DEADSEA_TX_UPDATES_NOTIFY";
		public static final String UPDATE_SIGNAL_ADD = "add";
		public static final String UPDATE_SIGNAL_REMOVE = "remove";
		public static final String UPDATE_STRING_UPDATE = "update";
	}
	
	public static final String[] DEFAULT_REDIS_PUBLISH_CHANNELS = new String[] {
			XAConstant.REDIS_CHANNLE_NAMES.PREPARE,
			XAConstant.REDIS_CHANNLE_NAMES.COMMIT,
			XAConstant.REDIS_CHANNLE_NAMES.ROLLBACK,
			XAConstant.REDIS_CHANNLE_NAMES.PREPARE + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE,
			XAConstant.REDIS_CHANNLE_NAMES.COMMIT + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE,
			XAConstant.REDIS_CHANNLE_NAMES.ROLLBACK + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE,
	};
	
	public static interface REDIS_KEY {
		public static final String TRASACTION_INFO_PREFIX = "XA_REDIS_TX_";
		public static final String ALL_TRANSACTIONS = "XA_REDIS_TX_IDS";
		public static final String TRANSACTIONS_SEQ_NO_LOCK = "XA_REDIS_TX_SEQ_LOCK_";
		public static final String TRANSACTIONS_SEQ_NO = "XA_REDIS_TX_SEQ_NO_";
		public static final String TRANSACTIONS_SEQ_LIMIT = "XA_REDIS_TX_SEQ_NO_LIMIT";
		public static final String TRANSACTIONS_SERVERS = "XA_REDIS_TX_SERVERS_";
		public static final String REDIS_SQL_IDS = "";
		//public static final String REALSERVER_
	}
	
	public static interface REDIS_LOCKS {

		public static final String TRANSACTION_LOCK = "XA_REDIS_TX_ID_LOCK_";
		public static final String TRANSACTIONS_LOCK = "XA_REDIS_TX_IDS_LOCK";
		public static final long DEFAULT_LOCK_TIMEOUT = 10000;
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
		public static final String TABLE_SEQUENCE_NO = "TABLE_SEQUENCE_NO";
		public static final String TX_SEQUENCE_NO = "TX_SEQUENCE_NO";
		public static final String PROPOSAL_ID = "PROPOSAL_ID";
		public static final String QUORUM = "TX_QUORUM";
		public static final String SQL_ID = "SQL_ID";
		public static final String IS_ERROR_TOLERATE = "ERROR_TOLERATE";
		public static final String TOLERATE_TIME = "TOLERATE_TIME";
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
		public static final String INSERT_SQL = "XA_CREATE_SQL_EXECUTOR";
	}
	
	public static final String LOG_KEY = "DEADSEA_SQL";
}
