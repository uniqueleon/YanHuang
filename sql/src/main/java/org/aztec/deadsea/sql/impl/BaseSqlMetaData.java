package org.aztec.deadsea.sql.impl;

import java.util.List;

import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.meta.Column;
import org.aztec.deadsea.sql.meta.Condition;
import org.aztec.deadsea.sql.meta.DataBase;
import org.aztec.deadsea.sql.meta.OrderByClause;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.aztec.deadsea.sql.meta.Table;

public class BaseSqlMetaData implements SqlMetaData {
	
	protected SqlType type;
	protected String rawSql;
	protected DataBase db;
	protected Table table;
	protected List<Condition> whereConditions;
	protected List<Column> groupByColumns;
	protected List<Condition> havingConditions;
	protected List<Table> fromTables;
	protected List<OrderByClause> orderByClauses;

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

	public DataBase getDatabase() {
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

	public DataBase getDb() {
		return db;
	}

	public void setDb(DataBase db) {
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

	
}
