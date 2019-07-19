package org.aztec.deadsea.sql.impl.xa;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaLogger;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAException.ErrorCodes;
import org.aztec.deadsea.common.xa.XAExecutor;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.common.xa.XAResponseBuilder;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.SqlGeneratorBuilder;
import org.aztec.deadsea.sql.conf.MetaDataTransformer;
import org.aztec.deadsea.sql.impl.executor.BaseSqlExecutor.ExecuteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetaDataRegistExecutor implements XAExecutor {


	@Autowired
	protected ServerRegister serverRegister;
	@Autowired
	protected MetaDataRegister metaRegister;
	@Autowired
	private XAResponseBuilder msgBuilder;
	@Autowired
	private ExecutorHelper helper;
	
	public MetaDataRegistExecutor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canHandle(XAContext context)  {
		return  context.getContextType().equals(XAConstant.XA_PROPOSAL_TYPES.CREATE_SQL);
	}

	@Override
	public XAResponse prepare(XAContext context)  {
		// TODO Auto-generated method stub
		try {
			String sqlType = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQL_TYPE);
			if(sqlType.equals(ExecuteType.EXEC.name())) {
				GenerationParameter gp = helper.getGenerationParam(context);
				ShardingConfiguration conf = helper.getShardingConfiguration(context);
				Authentication auth = conf.getAuth();
				MetaData mData = MetaDataTransformer.transfer(auth, conf, gp,false);
				if(!metaRegister.exists(auth, mData)) {
					metaRegister.regist(auth, mData);
				}
			}
			return msgBuilder.buildSuccess(context.getTransactionID(), context.getAssignmentNo(), TransactionPhase.PREPARE);
		} catch (Exception e) {
			DeadSeaLogger.error("XA_SQL", e);
			return msgBuilder.buildFail(context.getTransactionID(), context.getAssignmentNo(),ErrorCodes.UNKONW_ERROR, TransactionPhase.PREPARE);
		}
	}

	@Override
	public XAResponse commit(XAContext context)  {
		return msgBuilder.buildSuccess(context.getTransactionID(), context.getAssignmentNo(), TransactionPhase.PREPARE);
	}

	@Override
	public XAResponse rollback(XAContext context) {

		try {
			String sqlType = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQL_TYPE);
			if(sqlType.equals(ExecuteType.EXEC.name())) {
				String sql = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQLS);
				GenerationParameter gp = helper.getGenerationParam(context);
				ShardingConfiguration conf = helper.getShardingConfiguration(context);
				Authentication auth = conf.getAuth();
				MetaData mData = MetaDataTransformer.transfer(auth, conf, gp,true);
				if(metaRegister.exists(auth, mData)) {
					metaRegister.remove(auth, mData);
				}
			}
			return msgBuilder.buildSuccess(context.getTransactionID(), context.getAssignmentNo(), TransactionPhase.ROLLBACK);
			
		} catch (Exception e) {
			DeadSeaLogger.error("XA_SQL", e);
			return msgBuilder.buildFail(context.getTransactionID(), context.getAssignmentNo(),ErrorCodes.UNKONW_ERROR, TransactionPhase.ROLLBACK);
		}
		//return msgBuilder.buildFail();
	}

}
