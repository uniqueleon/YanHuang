package org.aztec.deadsea.sql;

public interface SqlGeneratorBuilder {

	public GenerationParameter getGenerationParam(String sql) throws ShardingSqlException;
	public ShardingSqlGenerator build(GenerationParameter param) throws ShardingSqlException;
}
