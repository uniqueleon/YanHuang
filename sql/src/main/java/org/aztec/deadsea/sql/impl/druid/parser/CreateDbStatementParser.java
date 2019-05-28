package org.aztec.deadsea.sql.impl.druid.parser;

import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.impl.druid.DruidSqlParser;
import org.aztec.deadsea.sql.meta.Database;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement;

@Component
public class CreateDbStatementParser implements DruidSqlParser {

	public CreateDbStatementParser() {
		// TODO Auto-generated constructor stub
	}

	public boolean accept(SQLStatement sql) {
		return sql instanceof SQLCreateDatabaseStatement;
	}

	public DruidMetaData parse(SQLStatement sql) {
		DruidMetaData dmd = new DruidMetaData(sql.toString());
		SQLCreateDatabaseStatement scds = (SQLCreateDatabaseStatement) sql;
		dmd.setType(SqlType.CREATE_DATABASE);
		dmd.setDb(new Database(scds.getName().getSimpleName(),null,scds.getCharacterSet(),scds.getCollate()));
		return dmd;
	}

}
