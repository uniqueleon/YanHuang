package org.aztec.deadsea.sql.impl.druid;

import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;


public class SqlInfoExtractor {

	public SqlInfoExtractor() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static MySqlSchemaStatVisitor parseSql(String sql) {
		MySqlStatementParser parser = new MySqlStatementParser(sql);
		SQLSelectStatement select = (SQLSelectStatement) parser.parseStatement();
		MySqlSchemaStatVisitor statVisitor = new MySqlSchemaStatVisitor();
		select.accept(statVisitor);
		//statVisitor.get
		return statVisitor;
		//SQLQueryParser sqp = new SQLQueryParser(sql, , factory)
		//select.getSelect();
		/*SQLStatement statement = parser.parseStatement();
		MySqlInsertStatement insert = (MySqlInsertStatement)statement;*/
	}
	
	
	public static void main(String[] args) {

		parseSql("select * from base_user bu,base_item bi");
	}

}
