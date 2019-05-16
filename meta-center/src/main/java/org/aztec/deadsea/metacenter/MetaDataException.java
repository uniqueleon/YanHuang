package org.aztec.deadsea.metacenter;

import org.aztec.deadsea.common.DeadSeaException;

public class MetaDataException extends DeadSeaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2176286173486543111L;
	
	public static interface ErrorCodes {
		public static final int BASE_INFO_INIT_ERROR = 0x1000;
		public static final int META_DATA_PERSIT_ERROR = 0x1001;
		public static final int META_DATA_INFO_CONFLICT = 0x1002;
		public static final int META_DATA_ALREADY_EXISTS = 0x1003;
		public static final int META_DATA_NOT_EXISTS = 0x1004;
		public static final int META_DATA_ERROR = 0x1005;
		
		public static final int INCOMPATIBLE_SERVER_TYPE = 0x1010;
		public static final int UNSUPPORT_OPERATION = 0x1011;
		
		public static final int AUTHENTICATION_DUPLICATE_ERROR = 0x1020;
		public static final int AUTHENTICATE_FAIL = 0x1021;
		public static final int ADD_AUTHENTICATION_FAIL = 0x1022;
		public static final int AUTHENTICATE_NOT_EXISTS = 0x1023;
		public static final int NOT_AUTHORIZED = 0x1024;
	}
	
	public MetaDataException(int errorCode) {
		super(errorCode);
	}

}
