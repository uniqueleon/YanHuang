package org.aztec.deadsea.sql;

import java.util.List;

public interface ShardingSqlGenerator {
	
	public boolean accept(GenerationParameter param);
	public String generateSingle(GenerationParameter param) throws ShardingSqlException;
	public List<String> generateMulti(GenerationParameter param) throws ShardingSqlException;
}
