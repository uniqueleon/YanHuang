package org.aztec.deadsea.metacenter;

import org.aztec.deadsea.common.DeadSeaException;

public class MetaDataException extends DeadSeaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2176286173486543111L;
	
	public static interface ErrorCodes {
		public static final int BASE_INFO_INIT_ERROR = MODULE_OFFSET.META_CENTER + 0x0000;
		public static final int META_DATA_PERSIT_ERROR = MODULE_OFFSET.META_CENTER + 0x0001;
		public static final int META_DATA_INFO_CONFLICT = MODULE_OFFSET.META_CENTER + 0x0002;
		public static final int META_DATA_ALREADY_EXISTS = MODULE_OFFSET.META_CENTER + 0x0003;
		public static final int META_DATA_NOT_EXISTS = MODULE_OFFSET.META_CENTER + 0x0004;
		public static final int META_DATA_ERROR = MODULE_OFFSET.META_CENTER + 0x0005;
		
		public static final int INCOMPATIBLE_SERVER_TYPE = MODULE_OFFSET.META_CENTER + 0x0010;
		public static final int UNSUPPORT_OPERATION = MODULE_OFFSET.META_CENTER + 0x0011;
		
		public static final int AUTHENTICATION_DUPLICATE_ERROR = MODULE_OFFSET.META_CENTER + 0x0020;
		public static final int AUTHENTICATE_FAIL = MODULE_OFFSET.META_CENTER + 0x0021;
		public static final int ADD_AUTHENTICATION_FAIL = MODULE_OFFSET.META_CENTER +  0x0022;
		public static final int AUTHENTICATE_NOT_EXISTS = MODULE_OFFSET.META_CENTER +  0x0023;
		public static final int NOT_AUTHORIZED = MODULE_OFFSET.META_CENTER + 0x0024;
	}
	
	public MetaDataException(int errorCode) {
		super(errorCode);
	}

}
