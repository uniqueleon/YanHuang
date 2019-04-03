package org.aztec.deadsea.sql.impl.druid;

import java.util.List;

import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.meta.Column;
import org.aztec.deadsea.sql.meta.Condition;
import org.aztec.deadsea.sql.meta.OrderByClause;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.aztec.deadsea.sql.meta.Table;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

public class DruidMetaData implements SqlMetaData {
	
	private String rawSql;

	public DruidMetaData(String sql) {
		rawSql = sql;
	}
	
	
	public void init(MySqlSchemaStatVisitor visitor) {
		
	}

	public String getRawSql() {
		return rawSql;
	}

	public SqlType getSqlType() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Table> fromTables() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Condition> getWhereConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Condition> getHavingConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Column> getGroupByColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OrderByClause> getOrderByClauses() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
