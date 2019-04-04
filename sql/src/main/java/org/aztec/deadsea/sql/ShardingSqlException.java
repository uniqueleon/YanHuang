package org.aztec.deadsea.sql;

public class ShardingSqlException extends Exception {
	
	private int code;
	
	public static interface ErrorCodes {
		public static final int UNSUPPORT_OPERATION = 0x01;
		public static final int SQL_FORMAT_ERROR = 0x02;
		public static final int SHARDING_CONFIGURATION_UNAVAILABLE = 0x03;
	}

	public ShardingSqlException(int errorCode) {
		this.code = errorCode;
	}

	public ShardingSqlException(String message,int errorCode) {
		super(message);
		this.code = errorCode;
	}

	public ShardingSqlException(Throwable cause,int errorCode) {
		super(cause);
		this.code = errorCode;
	}

	public ShardingSqlException(String message, Throwable cause,int errorCode) {
		super(message, cause);
		this.code = errorCode;
	}


}
