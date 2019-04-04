package org.aztec.deadsea.sql.impl.druid.parser;

import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.impl.druid.DruidSqlParser;
import org.aztec.deadsea.sql.meta.Database;
import org.aztec.deadsea.sql.meta.Table;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;

@Component
public class CreateTableSqlParser implements DruidSqlParser {

	public CreateTableSqlParser() {
		// TODO Auto-generated constructor stub
	}

	public boolean accept(SQLStatement sql) {
		return sql instanceof SQLCreateTableStatement;
	}

	public DruidMetaData parse(SQLStatement sql) {
		
		SQLCreateTableStatement scts = (SQLCreateTableStatement) sql;
		SQLExprTableSource ts = scts.getTableSource();
		String tableExpr = ts.getExpr().toString();
		DruidMetaData dmd = new DruidMetaData(sql.toString());
		if(tableExpr.contains(".")) {
			String dbName = tableExpr.split(".")[0].replaceAll("`", "");
			String tablename = tableExpr.split(".")[1].replaceAll("`", "");
			dmd.setDb(new Database(dbName, null, null, null));
		}
		else {

			String tablename = tableExpr.replaceAll("`", "");
			dmd.setDb(null);
			//dmd.setTable(new Table(tableName));
		}
		return null;
	}

}
