package org.aztec.deadsea.sql;

import org.aztec.deadsea.common.DeadSeaException;

public class ShardingSqlException extends DeadSeaException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -649035823118591799L;

	public static interface ErrorCodes {
		public static final int UNSUPPORT_OPERATION = 0x01;
		public static final int SQL_FORMAT_ERROR = 0x02;
		public static final int SHARDING_CONFIGURATION_UNAVAILABLE = 0x03;
		public static final int NO_TABLE_SCHEME_FOUND = 0x04;
		public static final int NO_DATABASE_SCHEME_FOUND = 0x05;
	}
	
	public ShardingSqlException(int errorCode) {
		super(errorCode);
	}

}
