package org.aztec.deadsea.sql.impl.executor;

import java.util.List;
import java.util.Map;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.xa.DistributedTransactionManager;
import org.aztec.deadsea.common.xa.DistributedTransactionManager.TransactionResultBuilder;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAResponseSet;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.SqlExecuteResult;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.impl.BaseSqlExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class MultiSqlXAExecutor extends BaseSqlExecutor{
	
	@Autowired
	protected DistributedTransactionManager manager;
	

	public MultiSqlXAExecutor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SqlExecuteResult doExecute(List<String> sqls,List<String> rollbacks, List<ServerScheme> schemes, ExecuteType type) throws Exception {
		
		int quorum = schemes.size();
		Map<String,Object> attachments = Maps.newHashMap();
		attachments.put(XAConstant.CONTEXT_KEYS.CONNECT_ARGS, getConnectionArgs(schemes));
		attachments.put(XAConstant.CONTEXT_KEYS.EXECUTE_SQL, sqls.toArray(new String[sqls.size()]));
		attachments.put(XAConstant.CONTEXT_KEYS.ROLLBACK_SQL, rollbacks);
		attachments.put(XAConstant.CONTEXT_KEYS.RAW_SQLS, gp.getSqlMetaData().getRawSql());
		attachments.put(XAConstant.CONTEXT_KEYS.RAW_SQL_TYPE, type.name());
		return manager.submit(quorum, attachments, new TransactionResultBuilder<SqlExecuteResult>() {

			@Override
			public SqlExecuteResult buildCommit(XAResponseSet responseSet) {
				return new BaseSqlExecResult(true);
			}

			@Override
			public SqlExecuteResult buildRollBack(XAResponseSet responseSet) {
				return new BaseSqlExecResult(false);
			}
		});
	}
	
	
	
	private String[][] getConnectionArgs(List<ServerScheme> schemes){
		String[][] args = new String[schemes.size()][];
		for(int i = 0;i < schemes.size();i++){
			ServerScheme scheme = schemes.get(i);
			String[] arg = new String[] {scheme.getHost(),"" + scheme.getPort(),scheme.getAuthority().getUsername(),
					scheme.getAuthority().getPassword()};
			args[i] = arg;
		}
		return args;
	}
	

	@Override
	protected void registMetaData(Authentication auth, ShardingConfiguration conf, GenerationParameter genParam)
			throws DeadSeaException {
		
	}


	
}
