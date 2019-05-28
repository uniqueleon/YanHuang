package org.aztec.deadsea.sql.impl.executor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.DeadSeaLogger;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.sql.ConnectionConfiguration;
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
import org.aztec.deadsea.sql.impl.BaseSqlExecResult;
import org.aztec.deadsea.sql.impl.DruidConnectPropertyPlaceHolder;
import org.aztec.deadsea.sql.impl.druid.DruidConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class DuridSqlExecutor implements ShardSqlExecutor {
	
	private static enum ExecuteType{
		EXEC,QUERY,UPDATE;
	}

	@Autowired
	SqlGeneratorBuilder builder;
	@Autowired
	ServerRegister serverRegister;
	@Autowired
	MetaDataRegister metaRegister;
	@Autowired
	ShardingConfigurationFactory confFactory;

	public DuridSqlExecutor() {
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
	
	public SqlExecuteResult execute(String sql,ExecuteMode mode,ExecuteType type) throws ShardingSqlException {
		try {
			GenerationParameter gp = builder.getGenerationParam(sql);
			ShardingSqlGenerator sqlGen = builder.build(gp);
			List<String> multiSql = Lists.newArrayList();
			switch(mode) {
			case SINGLE:
				//生成多条sql，逐条发送到服务器
				multiSql.addAll(sqlGen.generateMulti(gp));
				break;
			case BATCH:
				//生成一条sql，批量发到服务器
				multiSql.add(sqlGen.generateSingle(gp));
				break;
			}
			ShardingConfiguration conf = confFactory.getConfiguration();
			ShardingAge age = conf.getCurrentAge();
			List<ServerScheme> servers = conf.getRealServers(age.getNo());
			SqlExecuteResult result = new BaseSqlExecResult(true);
			for(ServerScheme server : servers) {
				result.merge(executeSqlInServer(multiSql, server,type));
			}
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
	
	public void registMetaData(Authentication auth,ShardingConfiguration conf,GenerationParameter genParam) throws DeadSeaException {
		metaRegister.regist(auth, MetaDataTransformer.transfer(auth, conf, genParam));
		
	}
	
	private String getConnectionID(ServerScheme server) {
		StringBuilder builder = new StringBuilder();
		builder.append(server.getHost() + "_" + server.getPort());
		return builder.toString();
	}
	
	public SqlExecuteResult executeSqlInServer(List<String> sqls,ServerScheme server,ExecuteType type) throws ShardingSqlException, IOException, SQLException {
		DruidConnector connector = new DruidConnector();
		BaseSqlExecResult sqlResult = new BaseSqlExecResult(true);
		Map<String,String> connectParam = Maps.newHashMap();
		connectParam.put(DruidConnectPropertyPlaceHolder.SERVER_HOST, server.getHost());
		connectParam.put(DruidConnectPropertyPlaceHolder.SERVER_PORT, "" + server.getPort());
		connectParam.put(DruidConnectPropertyPlaceHolder.USER_NAME, server.getAuthority().getUsername());
		connectParam.put(DruidConnectPropertyPlaceHolder.PASSWORD, server.getAuthority().getPassword());
		InputStream tmplInput = DuridSqlExecutor.class.getResource("/druid_connect.tmpl").openStream();
		Connection connection = connector.connect(new ConnectionConfiguration(getConnectionID(server),tmplInput,connectParam));
		Statement statement = connection.createStatement();
		//connector.connect(conf)
		int affectRow = 0;
		try {
			for(String sql : sqls) {
				switch(type) {
				case EXEC:
					statement.execute(sql);
					sqlResult.success();
					break;
				case QUERY:
					sqlResult.appendResult(statement.executeQuery(sql));
					sqlResult.success();
					break;
				case UPDATE:
					affectRow += statement.executeUpdate(sql);
					sqlResult.success();
					break;
				}
				
			}
			sqlResult.setAffectRow(affectRow);
			sqlResult.finish(sqls.size(), true);
		} catch (Exception e) {
			sqlResult.fail();
			sqlResult.finish(sqls.size(), false);
		}
		return sqlResult;
	}

}
