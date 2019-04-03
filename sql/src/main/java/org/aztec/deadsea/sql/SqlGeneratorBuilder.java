package org.aztec.deadsea.sql;

public interface SqlGeneratorBuilder {

	public GenerationParameter getGenerationParam(String sql,ShardingConfigurationBuilder scBuilder);
	public ShardingSqlGenerator build(GenerationParameter param);
}
