package org.aztec.deadsea.sql.impl.xa;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAExecutor;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.common.xa.XAResponseBuilder;
import org.aztec.deadsea.sql.impl.druid.DruidConnector;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

public class XASQLExecutor implements XAExecutor {

	private static final Map<String, Connection> connections = Maps.newConcurrentMap();
	@Autowired
	private XAResponseBuilder builder;

	public XASQLExecutor() {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public XAResponse prepare(XAContext context) throws XAException {
		// TODO Auto-generated method stub
		// Connection conn = get
		try {
			DruidConnector connector = new DruidConnector();
			Connection conn = connector.getConnection((String[])context.get("CONNECTION_ARGS"));
			conn.setAutoCommit(false);
			Statement stm = conn.createStatement();
			String sql = (String) context.get("EXECUTE_SQL");
			stm.execute(sql);
			XAResponse response = builder.buildSuccess(context.getTransactionID(), context.getAssignmentNo(),
					context.getCurrentPhase());
			connections.put(context.getTransactionID(), conn);
			return response;
		} catch (Exception e) {
			XAResponse response = builder.buildFail(context.getTransactionID(), context.getAssignmentNo(),e,
					context.getCurrentPhase());
			return response;
		}
	}

	@Override
	public XAResponse commit(XAContext context) throws XAException {
		// TODO Auto-generated method stub
		try {
			Connection connection = connections.get(context.getTransactionID());
			connection.commit();
			XAResponse response = builder.buildSuccess(context.getTransactionID(), context.getAssignmentNo(),
					context.getCurrentPhase());
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
			connection.rollback();
			XAResponse response = builder.buildSuccess(context.getTransactionID(), context.getAssignmentNo(),
					context.getCurrentPhase());
			return response;
		} catch (SQLException e) {
			XAResponse response = builder.buildFail(context.getTransactionID(), context.getAssignmentNo(), e,
					context.getCurrentPhase());
			return response;
		}
	}

}
