package org.aztec.deadsea.sql.impl.druid.parser;

import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlException.ErrorCodes;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.meta.Database;
import org.aztec.deadsea.sql.meta.Location;
import org.aztec.deadsea.sql.meta.Table;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;

public class ParserHelper {

	public ParserHelper() {
		// TODO Auto-generated constructor stub
	}

	public static DruidMetaData getMetaData(String  rawSql,SQLExprTableSource ts) throws ShardingSqlException {
		String tableExpr = ts.getExpr().toString();
		DruidMetaData dmd = new DruidMetaData(rawSql);
		dmd.setType(SqlType.CREATE_TABLE);
		if(tableExpr.contains(".")) {
			String dbName = tableExpr.split("\\.")[0].replaceAll("`", "");
			String tablename = tableExpr.split("\\.")[1].replaceAll("`", "");
			Database db = new Database(dbName, null, null, null);
			dmd.setDb(db);
			Table table = new Table(db, tablename, null, Location.FROM);
			dmd.setTable(table);
			return dmd;
		}
		else {
			throw new ShardingSqlException(ErrorCodes.NO_DATABASE_INFO);
		}
	}
}
