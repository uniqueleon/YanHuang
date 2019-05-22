package org.aztec.deadsea.sql.impl;

import java.util.List;

import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlGeneratorBuilder;
import org.aztec.deadsea.sql.impl.druid.DruidMetaDataBuilder;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DruidSqlGeneratorBuilder implements SqlGeneratorBuilder {
	
	@Autowired
	private ShardingConfigurationFactory confFactory;
	@Autowired
	private DruidMetaDataBuilder builder;
	@Autowired
	private List<ShardingSqlGenerator> generators;

	public DruidSqlGeneratorBuilder() {
		// TODO Auto-generated constructor stub
	}

	public GenerationParameter getGenerationParam(String sql) throws ShardingSqlException {
		try {
			SqlMetaData metaData = builder.getMetaData(sql);
			ShardingConfiguration conf = confFactory.getConfiguration();
			GenerationParam gp = new GenerationParam(metaData);
			return gp;
		} catch (DeadSeaException e) {
			throw new ShardingSqlException(e.getCode());
		}
	}

	public ShardingSqlGenerator build(GenerationParameter param) {
		for(ShardingSqlGenerator generator : generators) {
			if(generator.accept(param)) {
				return generator;
			}
		}
		return null;
	}

}
