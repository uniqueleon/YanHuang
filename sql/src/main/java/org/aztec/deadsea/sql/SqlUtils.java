package org.aztec.deadsea.sql;

import java.util.List;

import com.google.common.collect.Lists;

public class SqlUtils {

	public SqlUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<String> getMultiDatabaseTableNames(int dbSize,int tableSize,String dbname,String tableName){

		int dbPaddingSize = StringUtils.getLeasePadding(dbSize);
		int tablePaddingSize = StringUtils.getLeasePadding(tableSize);
		
		List<String> retList = Lists.newArrayList();
		for (int i = 0; i < dbSize; i++) {
			// String dbName =
			String dbPrefix = "`" + dbname + "_" + StringUtils.padding(true, "" + i, dbPaddingSize, '0') + "`";
			for (int j = 0; j < tableSize; j++) {
				String tmp = dbPrefix + ".`" + tableName + "_" + StringUtils.padding(true, "" + j, tablePaddingSize, '0') + "`";
				retList.add(tmp);
			}
		}
		return retList;
	}
	
	public static List<String> getMultiDatabaseNames(String dbName,int dbSize){
		int dbPaddingSize = StringUtils.getLeasePadding(dbSize);
		
		List<String> retList = Lists.newArrayList();
		for (int i = 0; i < dbSize; i++) {
			// String dbName =
			String dbPrefix = "`" + dbName + "_" + StringUtils.padding(true, "" + i, dbPaddingSize, '0') + "`";
			retList.add(dbPrefix);
		}
		return retList;
	}

	public static void main(String[] args) {
		System.out.println(getMultiDatabaseTableNames(7, 103, "lmdb", "account"));
	}
}
