package org.aztec.deadsea.sql;

public enum SqlType {
	QUERY,INSERT,UPDATE,CREATE_TABLE,CREATE_DATABASE,DROP_TABLE,DROP_DATABASE,ALTER_TABLE;
	
	public SqlType getAntiType() {
		switch(this) {
		case CREATE_DATABASE:
			return DROP_DATABASE;
		case CREATE_TABLE:
			return DROP_TABLE;
		}
		return null;
	}
}
