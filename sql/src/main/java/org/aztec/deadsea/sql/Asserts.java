package org.aztec.deadsea.sql;

import java.util.List;

public class Asserts {

	public Asserts() {
		// TODO Auto-generated constructor stub
	}

	public static void assertNotNull(Object object,int errorCode) throws ShardingSqlException{
		if(object == null)
			throw new ShardingSqlException(errorCode);
	}
	
	public static enum CompareType{
		GREATE,LESS,GE,LE,EQUAL;
	}
	
	public static void assertSize(List testList,Integer size,CompareType type,int errorCode) throws ShardingSqlException{
		boolean isValid = false;
		switch(type) {
		case GREATE:
			isValid =  testList.size() > size;
			break;
		case GE:
			isValid =  testList.size() >= size;
			break;
		case LESS:
			isValid =  testList.size() < size;
			break;
		case LE:
			isValid =  testList.size() <= size;
			break;
		case EQUAL:
			isValid = testList.size() == size;
			break;
			
		}
		if(!isValid) {
			throw new ShardingSqlException(errorCode);
		}
	}
}
