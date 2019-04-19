package org.aztec.deadsea.metacenter.conf;

public interface BaseConfDefaultValues {

	public static final int DB_SIZE = 11;
	public static final int TABLE_SIZE = 101;
	public static final long LOAD_SERVER_TIME_OUT = 3000;
	public static final long LOAD_TABLE_TIME_OUT = 3000;
	public static final long LOAD_AGE_TIME_OUT = 3000;
	public static final long INIT_SERVER_NUM = 1l;
	public static final long INIT_VIRTUAL_SERVER_NUM = 1l;
	public static final int TABLE_NUM = 0;
	public static final long MODULUS_LOWER_LIMIT = 0;
	public static final long MODULUS_UPPER_LIMIT = 0;
	public static final String[] SERVER_TYPES = {"db","cache"}; 
}
