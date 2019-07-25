package org.aztec.deadsea.sql.impl.executor;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
import org.aztec.deadsea.sql.impl.executor.XASqlExecuteParameter.SQLPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;

@Component
public class MultiSqlXAExecutor extends BaseSqlExecutor{
	
	@Autowired
	protected DistributedTransactionManager manager;
	

	public MultiSqlXAExecutor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SqlExecuteResult doExecute(XASqlExecuteParameter executeParam) throws Exception {
		List<ServerScheme> schemes = executeParam.getSchemes();
		int quorum = schemes.size();
		Map<String,Object> attachments = Maps.newHashMap();
		attachments.put(XAConstant.CONTEXT_KEYS.CONNECT_ARGS, getConnectionArgs(schemes));
		attachments.put(XAConstant.CONTEXT_KEYS.EXECUTE_SQL, toList(executeParam, true));
		attachments.put(XAConstant.CONTEXT_KEYS.ROLLBACK_SQL, toList(executeParam, false));
		attachments.put(XAConstant.CONTEXT_KEYS.RAW_SQLS, gp.getSqlMetaData().getRawSql());
		attachments.put(XAConstant.CONTEXT_KEYS.RAW_SQL_TYPE, type.name());
		attachments.put(XAConstant.CONTEXT_LOCAL_KEYS.GENENRATION_PARAMS,gp);
		attachments.put(XAConstant.CONTEXT_KEYS.SEQUENCE_NO, gp.getSqlMetaData().getSequenceNo());
		attachments.put(XAConstant.CONTEXT_LOCAL_KEYS.SHARDING_CONFIGURATION, conf);
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
	
	
	private List<List<String>> toList(XASqlExecuteParameter param,boolean sql){
		List<List<String>> retList = Lists.newArrayList();
		param.getPairs().stream().forEach(t -> {
			List<String> newList = Lists.newArrayList();
			t.stream().forEach(q -> {
				newList.add(sql ? q.getSql() : q.getRollback());
			});
			retList.add(newList);
		});
		return retList;
	}
	
	private String[][] toArray(XASqlExecuteParameter param,boolean sql){
		String[][] retSqls = new String[param.getPairs().size()][];
		for(int i = 0;i < param.getPairs().size();i++) {
			List<SQLPair> pair = param.getPairs().get(i);
			String[] sqlArr = new String[pair.size()];
			for(int j = 0;j < pair.size();j++) {
				sqlArr[j] = sql ? pair.get(j).getSql(): pair.get(j).getRollback();
			}
			retSqls[i] = sqlArr;
		}
		return retSqls;
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
