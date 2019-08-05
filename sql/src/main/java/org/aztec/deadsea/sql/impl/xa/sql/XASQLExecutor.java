package org.aztec.deadsea.sql.impl.xa.sql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class XASQLExecutor extends BaseSQLExecutor {


	public XASQLExecutor() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public boolean canHandle(XAContext context) {
		return context.getContextType().equals(XAConstant.XA_PROPOSAL_TYPES.CREATE_SQL);
	}

	@Override
	protected void doPrepare(XAContext context, List<String> sqls, Connection connection) throws Exception {
		Statement stm = connection.createStatement();
		context.get(XAConstant.CONTEXT_KEYS.EXECUTE_SQL);
		for(String sql : sqls) {
			System.out.println(sql);
			stm.execute(sql);
		}
	}

}
