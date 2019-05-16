package org.aztec.deadsea.common;

public class DeadSeaException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2362016262685337063L;
	protected int code;
	
	public static interface GeneralErrors{
		public static final int NEW_SERVER_SIZE_INCORRECT = 0xf0001;
		public static final int SCALE_ALGORITHM_ERROR = 0xf0002;
		public static final int UNSUPPORTED_OPERATION_PARAMETER = 0xf0003;
		public static final int UNSUPPORTED_OPERATION = 0xf0004;
	}

	public DeadSeaException() {
		// TODO Auto-generated constructor stub
	}
	

	public DeadSeaException(int errorCode) {
		super("ERROR CODE:" + errorCode);
		this.code = errorCode;
	}

	public DeadSeaException(String message,int errorCode) {
		super(message + "-----" + message);
		this.code = errorCode;
	}

	public DeadSeaException(Throwable cause,int errorCode) {
		super(cause);
		this.code = errorCode;
	}

	public DeadSeaException(String message, Throwable cause,int errorCode) {
		super(message, cause);
		this.code = errorCode;
	}

	public DeadSeaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,int errorCode) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.code = errorCode;
	}


	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}

}
