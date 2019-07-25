package org.aztec.deadsea.metacenter.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.aztec.autumn.common.utils.StringUtils;
import org.aztec.autumn.common.utils.jdbc.JdbcConnector;
import org.aztec.deadsea.common.sql.SQLTemplates;

import com.google.common.collect.Lists;

public class DatabaseUtils {

	public DatabaseUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getDataSize(String dbPrefix,String host,Integer dbSize,Integer tableSize) {
		Long retSize = new Long(1);
		return retSize;
	}
	
	public static List<String> generateCreateDbSql(String dbPrefix,Integer dbSize,String charset,String collate) {
		List<String> generateSql = Lists.newArrayList();
		for(int i = 0;i < dbSize;i++) {

			int paddingSize = StringUtils.getLeasePadding(dbSize);
			String dbName = dbPrefix + "_" + StringUtils.padding(true, "" + i, paddingSize, '0');
			String sql = String.format(SQLTemplates.CREATE_DATABASE,new Object[] {dbName,"utf8","utf8_general_ci"});
			generateSql.add(sql);
		}
		return generateSql;
	}
	
	public static void main(String[] args) {
		if(args.length <= 0) {
			System.err.println("Should be provide at least 1 parameter!");
			System.exit(0);
		}
		switch(args[0]) {
		case "GEN_SQL":
			List<String> sqls = generateCreateDbSql(args[1],Integer.parseInt(args[2]),args[3],args[4]);
			for(String sql : sqls) {
				System.out.println(sql);
			}
			break;
		
		}
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
