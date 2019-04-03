package org.aztec.deadsea.sql.meta;

import java.util.List;

import org.aztec.deadsea.sql.SqlType;

public interface SqlMetaData {


	public String getRawSql();
	public SqlType getSqlType();
	public Table getTable();
	public List<Table> fromTables();
	public List<Condition> getWhereConditions();
	public List<Condition> getHavingConditions();
	public List<Column> getGroupByColumns();
	public List<OrderByClause> getOrderByClauses();
	public DataBase getDatabase();
	
}
