package org.aztec.deadsea.sql.meta;

import java.util.List;

import org.aztec.deadsea.sql.SqlType;

public interface SqlMetaData {


	public List<Long> getTargetIds();
	public void setTargetIds(List<Long> targetIds);
	public boolean shard();
	public Integer getShardSize();
	public Long getSequenceNo();
	public Long getLastSequenceNo();
	public void setLastSequenceNo(Long seqNo);
	public void setSequenceNo(Long seqNo);
	public String getSourceSql();
	public String getRawSql();
	public SqlType getSqlType();
	public Table getTable();
	public List<Table> fromTables();
	public List<Condition> getWhereConditions();
	public List<Condition> getHavingConditions();
	public List<Column> getGroupByColumns();
	public List<Column> getInsertColumns();
	public List<OrderByClause> getOrderByClauses();
	public Database getDatabase();
	
}
