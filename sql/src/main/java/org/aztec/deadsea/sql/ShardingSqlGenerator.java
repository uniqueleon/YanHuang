package org.aztec.deadsea.sql;

public interface ShardingSqlGenerator {

	public static enum SqlType{
		QUERY,INSERT,UPDATE,CREAT_TABLE;
	}
	
	public String generate(GenerationParam param);
	
}