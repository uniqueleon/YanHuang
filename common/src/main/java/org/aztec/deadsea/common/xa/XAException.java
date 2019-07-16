package org.aztec.deadsea.common.xa;

import org.aztec.deadsea.common.DeadSeaException;

public class XAException extends DeadSeaException{
	
	public static interface ErrorCodes {
		public static final int GET_LOCAL_HOST_INFO_ERROR = MODULE_OFFSET.XA + 0x000;
		public static final int GET_LOCAL = MODULE_OFFSET.XA + 0x000;
		public static final int UNKONW_ERROR = MODULE_OFFSET.XA + 0xFFFF;
	}

	public XAException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XAException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

	public XAException(String message, int errorCode) {
		super(message, errorCode);
		// TODO Auto-generated constructor stub
	}

	public XAException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
			int errorCode) {
		super(message, cause, enableSuppression, writableStackTrace, errorCode);
		// TODO Auto-generated constructor stub
	}

	public XAException(String message, Throwable cause, int errorCode) {
		super(message, cause, errorCode);
		// TODO Auto-generated constructor stub
	}

	public XAException(Throwable cause, int errorCode) {
		super(cause, errorCode);
		// TODO Auto-generated constructor stub
	}

	

}
