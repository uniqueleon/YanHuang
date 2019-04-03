package org.aztec.deadsea.sql.impl.druid.parser;

import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.impl.druid.DruidSqlParser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;

public class AlterTableSqlParser implements DruidSqlParser {

	public AlterTableSqlParser() {
	}

	public boolean accept(SQLStatement sql) {
		return sql instanceof SQLAlterTableStatement;
	}

	public DruidMetaData parse(SQLStatement sql) {
		// TODO Auto-generated method stub
		return null;
	}

}
