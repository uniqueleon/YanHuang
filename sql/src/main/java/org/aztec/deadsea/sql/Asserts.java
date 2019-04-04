package org.aztec.deadsea.sql;

public class Asserts {

	public Asserts() {
		// TODO Auto-generated constructor stub
	}

	public static void assertNotNull(Object object,int errorCode) throws ShardingSqlException{
		if(object == null)
			throw new ShardingSqlException(errorCode);
	}
}
