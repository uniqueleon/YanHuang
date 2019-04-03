package org.aztec.deadsea.sql.impl.druid.parser;

import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.impl.druid.DruidSqlParser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;

public class CreateTableSqlParser implements DruidSqlParser {

	public CreateTableSqlParser() {
		// TODO Auto-generated constructor stub
	}

	public boolean accept(SQLStatement sql) {
		return sql instanceof SQLCreateTableStatement;
	}

	public DruidMetaData parse(SQLStatement sql) {
		
		SQLCreateTableStatement scts = (SQLCreateTableStatement) sql;
		//TableSource ts = scts.getTableSource();
		return null;
	}

}
