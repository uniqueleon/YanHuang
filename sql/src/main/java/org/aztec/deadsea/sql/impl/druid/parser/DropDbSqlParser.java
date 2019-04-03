package org.aztec.deadsea.sql.impl.druid.parser;

import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.impl.druid.DruidSqlParser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;

public class DropDbSqlParser implements DruidSqlParser {

	public DropDbSqlParser() {
		// TODO Auto-generated constructor stub
	}

	public boolean accept(SQLStatement sql) {
		return sql instanceof SQLDropDatabaseStatement;
	}

	public DruidMetaData parse(SQLStatement sql) {
		// TODO Auto-generated method stub
		return null;
	}

}
