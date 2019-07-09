package org.aztec.deadsea.sql.impl.xa;

public interface XAConstant {

	public static interface CONTEXT_KEYS{
		public static final String CONNECT_ARGS = "CONNECT_ARGS";
		public static final String EXECUTE_SQL = "TARGET_SQL";
		public static final String ROLLBACK_SQL = "ROLLBACK_SQL";
	}
}
