package org.aztec.deadsea.sql.impl.druid.parser;

import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.impl.druid.DruidSqlParser;
import org.aztec.deadsea.sql.meta.Table;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

@Component
public class SelectSqlParser implements DruidSqlParser {

	public SelectSqlParser() {
		// TODO Auto-generated constructor stub
	}

	public boolean accept(SQLStatement sql) {
		return sql instanceof SQLSelectStatement;
	}

	public DruidMetaData parse(SQLStatement sql) {

		SQLSelectStatement select = (SQLSelectStatement) sql;
		MySqlSchemaStatVisitor statVisitor = new MySqlSchemaStatVisitor();
		DruidMetaData dmd = new DruidMetaData(sql.toString());
		//dmd.setTable(new TableImpl(statVisitor.getCurrentTable()));
		return null;
	}

}
