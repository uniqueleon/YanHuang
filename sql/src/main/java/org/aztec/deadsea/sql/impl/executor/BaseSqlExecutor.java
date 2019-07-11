package org.aztec.deadsea.sql.impl.executor;

import java.io.IOException;
import java.util.List;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.DeadSeaLogger;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardSqlExecutor;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlException.ErrorCodes;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlExecuteResult;
import org.aztec.deadsea.sql.SqlGeneratorBuilder;
import org.aztec.deadsea.sql.SqlModularConstant;
import org.aztec.deadsea.sql.conf.MetaDataTransformer;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

public abstract class BaseSqlExecutor implements ShardSqlExecutor {

	protected static enum ExecuteType{
		EXEC,QUERY,UPDATE;
	}

	@Autowired
	protected SqlGeneratorBuilder builder;
	@Autowired
	protected ServerRegister serverRegister;
	@Autowired
	protected MetaDataRegister metaRegister;
	@Autowired
	protected ShardingConfigurationFactory confFactory;

	public SqlGeneratorBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(SqlGeneratorBuilder builder) {
		this.builder = builder;
	}

	public ServerRegister getServerRegister() {
		return serverRegister;
	}

	public void setServerRegister(ServerRegister serverRegister) {
		this.serverRegister = serverRegister;
	}

	public MetaDataRegister getMetaRegister() {
		return metaRegister;
	}

	public void setMetaRegister(MetaDataRegister metaRegister) {
		this.metaRegister = metaRegister;
	}

	public ShardingConfigurationFactory getConfFactory() {
		return confFactory;
	}

	public void setConfFactory(ShardingConfigurationFactory confFactory) {
		this.confFactory = confFactory;
	}

	@Override
	public SqlExecuteResult execute(String sql,ExecuteMode mode) throws ShardingSqlException {
		return execute(sql, mode, ExecuteType.EXEC);
	}
	
	public SqlExecuteResult executeQuery(String sql,ExecuteMode mode) throws ShardingSqlException {
		return execute(sql, mode, ExecuteType.QUERY);
	}
	
	public SqlExecuteResult executeUpdate(String sql,ExecuteMode mode) throws ShardingSqlException {
		return execute(sql, mode, ExecuteType.UPDATE);
	}
	
	protected SqlExecuteResult execute(String sql,ExecuteMode mode,ExecuteType type) throws ShardingSqlException {
		try {
			GenerationParameter gp = builder.getGenerationParam(sql);
			ShardingSqlGenerator sqlGen = builder.build(gp);
			List<String> multiSql = Lists.newArrayList();
			List<String> rollback = Lists.newArrayList();
			switch(mode) {
			case SINGLE:
				//生成多条sql，逐条发送到服务器
				multiSql.addAll(sqlGen.generateMulti(gp));
				rollback.addAll(sqlGen.generateRollback(gp));
				break;
			case BATCH:
				//生成一条sql，批量发到服务器
				multiSql.add(sqlGen.generateSingle(gp));
				rollback.addAll(sqlGen.generateRollback(gp));
				break;
			}
			ShardingConfiguration conf = confFactory.getConfiguration();
			ShardingAge age = conf.getCurrentAge();
			List<ServerScheme> servers = conf.getRealServers(age.getNo());
			SqlExecuteResult result = doExecute(multiSql,rollback, servers,type);
			
			if(type.equals(ExecuteType.EXEC) && result.isSuccess()) {
				registMetaData(conf.getAuth(), conf, gp);
			}
			return result;
		} catch (DeadSeaException e) {
			DeadSeaLogger.error(SqlModularConstant.LOG_PREFIX, e);
			throw new ShardingSqlException(e,e.getCode());
		}catch (IOException e) {
			DeadSeaLogger.error(SqlModularConstant.LOG_PREFIX, e);
			throw new ShardingSqlException(e,ErrorCodes.UNKOWN_ERROR);
		}catch (Exception e) {
			DeadSeaLogger.error(SqlModularConstant.LOG_PREFIX, e);
			throw new ShardingSqlException(e,ErrorCodes.UNKOWN_ERROR);
		}
	}
	
	protected void registMetaData(Authentication auth,ShardingConfiguration conf,GenerationParameter genParam) throws DeadSeaException {
		metaRegister.regist(auth, MetaDataTransformer.transfer(auth, conf, genParam));
		
	}
	
	protected String getConnectionID(ServerScheme server) {
		StringBuilder builder = new StringBuilder();
		builder.append(server.getHost() + "_" + server.getPort());
		return builder.toString();
	}
	
	protected abstract SqlExecuteResult doExecute(List<String> sqls,List<String> rollbacks,
			List<ServerScheme> scheme,ExecuteType type) throws Exception;	
}
