package org.aztec.deadsea.sql.impl;

import org.aztec.deadsea.sql.ShardingConfiguration;

public class GenerationParam {

	public GenerationParam() {
		// TODO Auto-generated constructor stub
	}
	private String rawSql;
	private String dbName;
	private String tableName;
	private int generatedSize = 0;
	private ShardingConfiguration conf;
	public GenerationParam(String rawSql, String dbName, String tableName, String[] generatedDbNames,
			String[] generatedTableNames,ShardingConfiguration conf) {
		super();
		this.rawSql = rawSql;
		this.dbName = dbName;
		this.tableName = tableName;
		generatedSize = generatedDbNames.length * generatedTableNames.length;
		this.conf = conf;
	}
	public int getGeneratedSize() {
		return generatedSize;
	}
	public void setGeneratedSize(int generatedSize) {
		this.generatedSize = generatedSize;
	}
	public String getRawSql() {
		return rawSql;
	}
	public void setRawSql(String rawSql) {
		this.rawSql = rawSql;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
