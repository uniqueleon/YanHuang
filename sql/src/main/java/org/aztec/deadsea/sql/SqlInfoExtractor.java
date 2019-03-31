package org.aztec.deadsea.sql;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExecuteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;


public class SqlInfoExtractor {

	public SqlInfoExtractor() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public static void parseSql(String sql) {
		MySqlStatementParser parser = new MySqlStatementParser(sql);
		SQLSelectStatement select = (SQLSelectStatement) parser.parseStatement();
		SQLSelect select2 = select.getSelect();
		SQLSelectQuery query = select2.getQuery();
		StringBuffer buf = new StringBuffer();
		query.output(buf);
		System.out.println(buf.toString());
		MySqlSelectQueryBlock qb = (MySqlSelectQueryBlock) query;
		
		SQLObject object = query.getParent();
		object.output(buf);
		System.out.println(object.toString());
		//SQLQueryParser sqp = new SQLQueryParser(sql, , factory)
		//select.getSelect();
		/*SQLStatement statement = parser.parseStatement();
		MySqlInsertStatement insert = (MySqlInsertStatement)statement;*/
	}
	
	
	public static void main(String[] args) {

		parseSql("select * from base_user");
	}

}
