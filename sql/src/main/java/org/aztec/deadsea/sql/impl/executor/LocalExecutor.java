package org.aztec.deadsea.sql.impl.executor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.aztec.deadsea.sql.ConnectionConfiguration;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.SqlExecuteResult;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.impl.BaseSqlExecResult;
import org.aztec.deadsea.sql.impl.DruidConnectPropertyPlaceHolder;
import org.aztec.deadsea.sql.impl.druid.DruidConnector;

import com.google.common.collect.Maps;

//@Component
public class LocalExecutor extends BaseSqlExecutor {
	
	public SqlExecuteResult doExecute(List<String> sqls,List<String> rollbacks,List<ServerScheme> servers,ExecuteType type) throws ShardingSqlException, IOException, SQLException {
		
		BaseSqlExecResult retResult = new BaseSqlExecResult(true);
		for(ServerScheme server : servers) {
			DruidConnector connector = new DruidConnector();
			BaseSqlExecResult sqlResult = new BaseSqlExecResult(true);
			Map<String,String> connectParam = Maps.newHashMap();
			connectParam.put(DruidConnectPropertyPlaceHolder.SERVER_HOST, server.getHost());
			connectParam.put(DruidConnectPropertyPlaceHolder.SERVER_PORT, "" + server.getPort());
			connectParam.put(DruidConnectPropertyPlaceHolder.USER_NAME, server.getAuthority().getUsername());
			connectParam.put(DruidConnectPropertyPlaceHolder.PASSWORD, server.getAuthority().getPassword());
			InputStream tmplInput = LocalExecutor.class.getResource("/druid_connect.tmpl").openStream();
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
				return sqlResult;
			}
			retResult.merge(sqlResult);
		}
		return retResult;
	}

}
