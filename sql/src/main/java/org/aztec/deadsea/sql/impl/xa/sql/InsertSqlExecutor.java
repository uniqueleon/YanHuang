package org.aztec.deadsea.sql.impl.xa.sql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.DeadSeaLogger;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.sql.BatchSequenceNumberManager;
import org.aztec.deadsea.sql.ShardingSqlConstants;
import org.aztec.deadsea.sql.impl.xa.ExecutorHelper;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InsertSqlExecutor extends BaseSQLExecutor{
	
	@Autowired
	ExecutorHelper helper;
	@Autowired
	BatchSequenceNumberManager sequenceNumberManger;

	public InsertSqlExecutor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canHandle(XAContext context) {
		return context.getContextType().equals(XAConstant.XA_PROPOSAL_TYPES.INSERT_SQL);
	}

	@Override
	protected void doPrepare(XAContext context, List<String> sqls, Connection connection) throws Exception {
		// TODO Auto-generated method stub
		Statement stm = connection.createStatement();
		stm.execute(sqls.get(context.getAssignmentNo()));
	}

	@Override
	public XAResponse commit(XAContext context) {
		try {
			SqlMetaData metaData = helper.getGenerationParam(context).getSqlMetaData();
			if(sequenceNumberManger.commit(metaData)) {
				sequenceNumberManger.release(metaData);
				return super.commit(context);
			}
		} catch (DeadSeaException e) {
			DeadSeaLogger.error(ShardingSqlConstants.LOG_PREFIX, e);
		}
		return super.rollback(context);
	}

	@Override
	public XAResponse rollback(XAContext context) {
		try {
			SqlMetaData metaData = helper.getGenerationParam(context).getSqlMetaData();
			sequenceNumberManger.cancel(metaData);
		} catch (DeadSeaException e) {
			DeadSeaLogger.error(ShardingSqlConstants.LOG_PREFIX, e);
		}
		return super.rollback(context);
	}

	
	
}
