package org.aztec.deadsea.sql.impl.xa;

import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlGeneratorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExecutorHelper {

	@Autowired
	protected ServerRegister serverRegister;
	@Autowired
	protected MetaDataRegister metaRegister;
	@Autowired
	protected ShardingConfigurationFactory confFactory;
	@Autowired
	protected SqlGeneratorBuilder builder;

	public ExecutorHelper() {
		// TODO Auto-generated constructor stub
	}

	public GenerationParameter getGenerationParam(XAContext context) throws ShardingSqlException {
		Object localGp = context.getLocal(XAConstant.CONTEXT_LOCAL_KEYS.GENENRATION_PARAMS);
		GenerationParameter gp = null;
		if (localGp == null) {
			String sql = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQLS);
			String sqlType = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQL_TYPE);
			gp = builder.getGenerationParam(sql);
		}
		else {
			gp = (GenerationParameter) localGp;
		}
		return gp;
	}

	public ShardingConfiguration getShardingConfiguration(XAContext context) throws ShardingSqlException {
		Object confObj = context
				.getLocal(XAConstant.CONTEXT_LOCAL_KEYS.SHARDING_CONFIGURATION);
		ShardingConfiguration conf;
		if (confObj == null) {
			conf = confFactory.getConfiguration();
		}
		else {
			conf = (ShardingConfiguration) context
					.getLocal(XAConstant.CONTEXT_LOCAL_KEYS.SHARDING_CONFIGURATION);
		}
		return conf;
	}

}
