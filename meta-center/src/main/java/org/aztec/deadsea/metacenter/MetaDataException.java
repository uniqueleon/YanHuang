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
		public static final int INCOMPATIBLE_SERVER_TYPE = 0x1003;
	}
	
	public MetaDataException(int errorCode) {
		super(errorCode);
	}

}
