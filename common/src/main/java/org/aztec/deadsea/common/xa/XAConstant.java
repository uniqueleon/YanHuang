package org.aztec.deadsea.common.xa;

public interface XAConstant {

	public static interface REDIS_CHANNLE_NAMES{

		public static final String SEPERATOR = "_";
		public static final String PREFIX = "XA_REDIS_CHANNEL_";
		public static final String PREPARE = PREFIX + "_PREPARE";
		public static final String COMMIT = PREFIX + "_COMMIT";
		public static final String ROLLBACK = PREFIX + "_ROLLBACK";
		public static final String ACKOWNLEDGE = "_ACK";
	}
	
	public static interface REDIS_KEY {
		public static final String TRASACTION_INFO_PREFIX = "XA_REDIS_TX_";
		public static final String TRANSACTIONS_ID = "XA_REDIS_TX_IDS";
		public static final String TRANSACTIONS_SEQ_NO_LOCK = "XA_REDIS_TX_SEQ_LOCK_";
		public static final String TRANSACTIONS_SEQ_NO = "XA_REDIS_TX_SEQ_NO_";
		public static final String TRANSACTIONS_SEQ_LIMIT = "XA_REDIS_TX_SEQ_NO_LIMIT";
	}
	
	public static interface CONTEXT_KEYS{
		public static final String PHASE = "phase";
	}
	
	public static interface MSG_BASE_RESPONSE{
		public static final String OK = "OK";
		public static final String FAIL = "FAIL";
	}
}
