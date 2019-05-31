package org.aztec.deadsea.sql.impl.xa;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAExecutor;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.common.xa.XAResponseBuilder;
import org.aztec.deadsea.sql.ConnectionConfiguration;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.impl.BaseSqlExecResult;
import org.aztec.deadsea.sql.impl.DruidConnectPropertyPlaceHolder;
import org.aztec.deadsea.sql.impl.druid.DruidConnector;
import org.aztec.deadsea.sql.impl.executor.DuridSqlExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

public class XASQLExecutor implements XAExecutor {

	private static final Map<String, Connection> connections = Maps.newConcurrentMap();
	@Autowired
	private XAResponseBuilder builder;

	public XASQLExecutor() {
		// TODO Auto-generated constructor stub
	}

	private Connection getConnection(String[] args) throws ShardingSqlException, IOException, SQLException {
		DruidConnector connector = new DruidConnector();
		BaseSqlExecResult sqlResult = new BaseSqlExecResult(true);
		Map<String, String> connectParam = Maps.newHashMap();
		connectParam.put(DruidConnectPropertyPlaceHolder.SERVER_HOST, args[0]);
		connectParam.put(DruidConnectPropertyPlaceHolder.SERVER_PORT, args[1]);
		connectParam.put(DruidConnectPropertyPlaceHolder.USER_NAME, args[2]);
		connectParam.put(DruidConnectPropertyPlaceHolder.PASSWORD, args[3]);
		InputStream tmplInput = DuridSqlExecutor.class.getResource("/druid_connect.tmpl").openStream();
		Connection connection = connector
				.connect(new ConnectionConfiguration(args[0] + "_" + args[1], tmplInput, connectParam));

		return connection;
	}

	@Override
	public XAResponse prepare(XAContext context) throws XAException {
		// TODO Auto-generated method stub
		// Connection conn = get
		return null;
	}

	@Override
	public XAResponse commit(XAContext context) throws XAException {
		// TODO Auto-generated method stub
		try {
			Connection connection = connections.get(context.getTransactionID());
			XAResponse response = builder.buildSuccess(context.getTransactionID(), context.getAssignmentNo(),
					context.getCurrentPhase());
			connection.commit();
			return response;
		} catch (SQLException e) {
			XAResponse response = builder.buildFail(context.getTransactionID(), context.getAssignmentNo(), e,
					context.getCurrentPhase());
			return response;
		}
	}

	@Override
	public XAResponse rollback(XAContext context) throws XAException {
		try {
			Connection connection = connections.get(context.getTransactionID());
			XAResponse response = builder.buildSuccess(context.getTransactionID(), context.getAssignmentNo(),
					context.getCurrentPhase());
			connection.rollback();
			return response;
		} catch (SQLException e) {
			XAResponse response = builder.buildFail(context.getTransactionID(), context.getAssignmentNo(), e,
					context.getCurrentPhase());
			return response;
		}
	}

}
