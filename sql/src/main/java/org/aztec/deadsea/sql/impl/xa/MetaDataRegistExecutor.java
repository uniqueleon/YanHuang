package org.aztec.deadsea.sql.impl.xa;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaLogger;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAException.ErrorCodes;
import org.aztec.deadsea.common.xa.XAExecutor;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.common.xa.XAResponseBuilder;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
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
	protected ShardingConfigurationFactory confFactory;
	@Autowired
	protected SqlGeneratorBuilder builder;
	@Autowired
	private XAResponseBuilder msgBuilder;
	
	public MetaDataRegistExecutor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canHandle(XAContext context) throws XAException {
		return  context.getContextType().equals(XAConstant.XA_PROPOSAL_TYPES.XA_SQL);
	}

	@Override
	public XAResponse prepare(XAContext context) throws XAException {
		// TODO Auto-generated method stub
		try {
			String sql = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQLS);
			String sqlType = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQL_TYPE);
			if(sqlType.equals(ExecuteType.EXEC.name())) {
				GenerationParameter gp = builder.getGenerationParam(sql);
				ShardingConfiguration conf = confFactory.getConfiguration();
				Authentication auth = conf.getAuth();
				MetaData mData = MetaDataTransformer.transfer(auth, conf, gp);
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
	public XAResponse commit(XAContext context) throws XAException {
		return msgBuilder.buildSuccess(context.getTransactionID(), context.getAssignmentNo(), TransactionPhase.PREPARE);
	}

	@Override
	public XAResponse rollback(XAContext context) throws XAException {

		try {
			String sqlType = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQL_TYPE);
			if(sqlType.equals(ExecuteType.EXEC.name())) {
				String sql = (String) context.get(XAConstant.CONTEXT_KEYS.RAW_SQLS);
				GenerationParameter gp = builder.getGenerationParam(sql);
				ShardingSqlGenerator sqlGen = builder.build(gp);
				ShardingConfiguration conf = confFactory.getConfiguration();
				Authentication auth = conf.getAuth();
				MetaData mData = MetaDataTransformer.transfer(auth, conf, gp);
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
