package org.aztec.deadsea.sql.impl.druid.parser;

import java.util.Map;

import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.impl.druid.DruidSqlParser;
import org.aztec.deadsea.sql.meta.Location;
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
		String tablename = statVisitor.getCurrentTable();
		//statVisitor.getTableStat(tablename).
		Map<String,String> aliasMap = statVisitor.getAliasMap();
		dmd.setTable(new Table(null,tablename,aliasMap.get(tablename),Location.FROM));
		dmd.setType(SqlType.QUERY);
		dmd.setDb(null);
		return dmd;
	}

}
