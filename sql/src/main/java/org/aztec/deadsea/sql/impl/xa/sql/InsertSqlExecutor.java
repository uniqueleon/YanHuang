package org.aztec.deadsea.sql.impl.xa.sql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
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
		return context.getContextType().equals(XAConstant.XA_PROPOSAL_TYPES.CREATE_SQL);
	}

	@Override
	protected void doPrepare(XAContext context, List<String> sqls, Connection connection) throws Exception {
		// TODO Auto-generated method stub
		String sql = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQLS);
		String sqlType = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQL_TYPE);
		Statement stm = connection.createStatement();
		if(sqlType.equals(ExecuteType.INSERT.name())) {
			stm.execute(sqlType);
		}
	}

}
