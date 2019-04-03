package org.aztec.deadsea.sql;

public interface SqlGeneratorBuilder {

	public GenerationParameter getGenerationParam(String sql);
	public ShardingSqlGenerator build(GenerationParameter param);
}
