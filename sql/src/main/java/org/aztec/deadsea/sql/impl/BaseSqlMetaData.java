package org.aztec.deadsea.sql.impl;

import java.util.List;

import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.meta.Column;
import org.aztec.deadsea.sql.meta.Condition;
import org.aztec.deadsea.sql.meta.Database;
import org.aztec.deadsea.sql.meta.OrderByClause;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.aztec.deadsea.sql.meta.Table;

public class BaseSqlMetaData implements SqlMetaData {

	protected boolean shard;
	protected Integer shardSize;
	protected Long sequenceNo;
	protected SqlType type;
	protected String rawSql;
	protected String sourceSql;
	protected Database db;
	protected Table table;
	protected List<Condition> whereConditions;
	protected List<Column> groupByColumns;
	protected List<Column> insertColumns;
	protected List<Condition> havingConditions;
	protected List<Table> fromTables;
	protected List<OrderByClause> orderByClauses;
	protected List<Long> targetIds;

	public BaseSqlMetaData() {
		// TODO Auto-generated constructor stub
	}

	public String getRawSql() {
		// TODO Auto-generated method stub
		return rawSql;
	}

	public SqlType getSqlType() {
		// TODO Auto-generated method stub
		return type;
	}

	public List<Table> fromTables() {
		// TODO Auto-generated method stub
		return fromTables;
	}

	public List<Condition> getWhereConditions() {
		// TODO Auto-generated method stub
		return whereConditions;
	}

	public List<Condition> getHavingConditions() {
		// TODO Auto-generated method stub
		return havingConditions;
	}

	public List<Column> getGroupByColumns() {
		return groupByColumns;
	}

	public List<OrderByClause> getOrderByClauses() {
		return orderByClauses;
	}

	public Database getDatabase() {
		// TODO Auto-generated method stub
		return db;
	}

	public Table getTable() {
		// TODO Auto-generated method stub
		return table;
	}

	public SqlType getType() {
		return type;
	}

	public void setType(SqlType type) {
		this.type = type;
	}

	public Database getDb() {
		return db;
	}

	public void setDb(Database db) {
		this.db = db;
	}

	public List<Table> getFromTables() {
		return fromTables;
	}

	public void setFromTables(List<Table> fromTables) {
		this.fromTables = fromTables;
	}

	public void setRawSql(String rawSql) {
		this.rawSql = rawSql;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public void setWhereConditions(List<Condition> whereConditions) {
		this.whereConditions = whereConditions;
	}

	public void setGroupByColumns(List<Column> groupByColumns) {
		this.groupByColumns = groupByColumns;
	}

	public void setHavingConditions(List<Condition> havingConditions) {
		this.havingConditions = havingConditions;
	}

	public void setOrderByClauses(List<OrderByClause> orderByClauses) {
		this.orderByClauses = orderByClauses;
	}

	public boolean isShard() {
		return shard;
	}

	public void setShard(boolean shard) {
		this.shard = shard;
	}

	@Override
	public boolean shard() {
		return shard;
	}

	public Integer getShardSize() {
		return shardSize;
	}

	public void setShardSize(Integer shardSize) {
		this.shardSize = shardSize;
	}

	public void setSourceSql(String sourceSql) {
		this.sourceSql = sourceSql;
	}

	@Override
	public String getSourceSql() {
		return sourceSql;
	}

	public List<Column> getInsertColumns() {
		return insertColumns;
	}

	public void setInsertColumns(List<Column> insertColumns) {
		this.insertColumns = insertColumns;
	}

	public List<Long> getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(List<Long> targetIds) {
		this.targetIds = targetIds;
	}

	@Override
	public Long getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Long sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	
}
