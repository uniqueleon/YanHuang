package org.aztec.deadsea.common;

public class DeadSeaException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2362016262685337063L;
	protected int code;

	public DeadSeaException() {
		// TODO Auto-generated constructor stub
	}
	

	public DeadSeaException(int errorCode) {
		super();
		this.code = errorCode;
	}

	public DeadSeaException(String message,int errorCode) {
		super(message);
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

}
