package org.aztec.deadsea.sql.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.aztec.autumn.common.utils.jdbc.JdbcConnector;
import org.aztec.deadsea.sql.impl.druid.DruidConnector;

import com.beust.jcommander.internal.Lists;

public class DbUtils {

	public DbUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static long countTotalDataSize(String[][] args,List<String> tablePrefixes) throws SQLException, ClassNotFoundException{
		long totalSize = 0l;
		for(String[] arg : args) {
			Connection conn = JdbcConnector.getConnection(arg[0], arg[1],arg[2], "mysql");
			totalSize += countTotalDataSize(conn, tablePrefixes);
		}
		return totalSize;
	}
	
	public static long countTotalDataSize(Connection connection,List<String> tablePrefixes) throws SQLException {
		
		long totalSize = 0l;
		Statement stm = connection.createStatement();
		ResultSet rs = stm.executeQuery(buildCountDataSizeSql(tablePrefixes));
		while(rs.next()) {
			return rs.getLong(1);
		}
		return totalSize;
	}
	
	private static String buildCountDataSizeSql(List<String> tablePrefixes) {
		StringBuilder countSql = new StringBuilder();
		countSql.append("SELECT sum(table_rows) FROM `information_schema`.`TABLES` ");
		for(String tablePrefix : tablePrefixes) {
			if(!countSql.toString().contains("WHERE")) {
				countSql.append(" WHERE ");
			}
			else {
				countSql.append(" OR ");
			}
			countSql.append(" table_name like '%" + tablePrefix + "%'");
		}
		return countSql.toString();
	}

	
}
