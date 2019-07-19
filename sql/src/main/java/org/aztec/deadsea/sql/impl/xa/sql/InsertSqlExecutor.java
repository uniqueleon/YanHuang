package org.aztec.deadsea.sql.impl.xa.sql;

import java.sql.Connection;
import java.util.List;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.impl.executor.BaseSqlExecutor.ExecuteType;
import org.aztec.deadsea.sql.impl.xa.ExecutorHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertSqlExecutor extends BaseSQLExecutor{
	
	@Autowired
	ExecutorHelper helper;

	public InsertSqlExecutor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canHandle(XAContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void doPrepare(XAContext context, List<String> sqls, Connection connection) throws Exception {
		// TODO Auto-generated method stub
		String sql = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQLS);
		String sqlType = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQL_TYPE);
		if(sqlType.equals(ExecuteType.INSERT.name())) {
			GenerationParameter gp = helper.getGenerationParam(context);
			ShardingConfiguration conf = helper.getShardingConfiguration(context);
			Authentication auth = conf.getAuth();
			
		}
	}

}
