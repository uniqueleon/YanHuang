package org.aztec.deadsea.sql.impl.executor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

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
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.impl.BaseSqlExecResult;
import org.aztec.deadsea.sql.impl.DruidConnectPropertyPlaceHolder;
import org.aztec.deadsea.sql.impl.druid.DruidConnector;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

public class DuridSqlExecutor implements ShardSqlExecutor {
	

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
	public SqlExecuteResult execute(String sql,ExecuteType type) throws ShardingSqlException {

		return null;
	}
	
	public SqlExecuteResult executeQuery(String sql,ExecuteType type) throws ShardingSqlException {

		return null;
	}
	
	public SqlExecuteResult executeUpdate(String sql,ExecuteType type) throws ShardingSqlException {

		return null;
	}
	
	public SqlExecuteResult execute(String sql,String executeType) throws ShardingSqlException {
		try {
			GenerationParameter gp = builder.getGenerationParam(sql);
			ShardingSqlGenerator sqlGen = builder.build(gp);
			List<String> multiSql = sqlGen.generateMulti(gp);
			ShardingConfiguration conf = confFactory.getConfiguration();
			ShardingAge age = conf.getCurrentAge();
			List<ServerScheme> servers = conf.getRealServers(age.getNo());
			for(ServerScheme server : servers) {
				executeSqlInServer(multiSql, server);
			}
			return new BaseSqlExecResult(true);
		} catch (DeadSeaException e) {
			DeadSeaLogger.error(SqlModularConstant.LOG_PREFIX, e);
			throw new ShardingSqlException(e.getCode());
		}catch (IOException e) {
			DeadSeaLogger.error(SqlModularConstant.LOG_PREFIX, e);
			throw new ShardingSqlException(e,ErrorCodes.UNKOWN_ERROR);
		}catch (Exception e) {
			DeadSeaLogger.error(SqlModularConstant.LOG_PREFIX, e);
			throw new ShardingSqlException(e,ErrorCodes.UNKOWN_ERROR);
		}
	}
	
	public void executeSqlInServer(List<String> sqls,ServerScheme server) throws ShardingSqlException, IOException, SQLException {
		DruidConnector connector = new DruidConnector();
		Map<String,String> connectParam = Maps.newHashMap();
		connectParam.put(DruidConnectPropertyPlaceHolder.SERVER_HOST, server.getHost());
		connectParam.put(DruidConnectPropertyPlaceHolder.SERVER_PORT, "" + server.getPort());
		connectParam.put(DruidConnectPropertyPlaceHolder.USER_NAME, server.getAuthority().getUsername());
		connectParam.put(DruidConnectPropertyPlaceHolder.PASSWORD, server.getAuthority().getPassword());
		InputStream tmplInput = DuridSqlExecutor.class.getResource("/druid_connect.tmpl").openStream();
		Connection connection = connector.connect(new ConnectionConfiguration(tmplInput,connectParam));
		Statement statement = connection.createStatement();
		//connector.connect(conf)
		for(String sql : sqls) {
			statement.execute(sql);
			statement.executeQuery(sql);
			statement.executeUpdate(sql);
		}
	}
	
	
	/*private Connection getConnection() {
		
	}*/
	
	

	@Override
	public SqlExecuteResult executeBatch(String sql) throws ShardingSqlException {

		GenerationParameter gp = builder.getGenerationParam(sql);
		ShardingSqlGenerator sqlGen = builder.build(gp);
		String integratedSql = sqlGen.generateSingle(gp);
		return null;
	}

}
