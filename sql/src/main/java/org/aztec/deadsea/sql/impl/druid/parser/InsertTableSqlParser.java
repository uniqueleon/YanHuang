package org.aztec.deadsea.sql.impl.druid.parser;

import java.util.List;

import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlException.ErrorCodes;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.impl.druid.DruidSqlParser;
import org.aztec.deadsea.sql.meta.Column;
import org.aztec.deadsea.sql.meta.Location;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.beust.jcommander.internal.Lists;

@Component
public class InsertTableSqlParser implements DruidSqlParser {

	public InsertTableSqlParser() {
	}

	public boolean accept(SQLStatement sql) {
		return sql instanceof SQLInsertStatement;
	}

	public DruidMetaData parse(SQLStatement sql) throws ShardingSqlException {
        MySqlInsertStatement insert = (MySqlInsertStatement)sql;
		DruidMetaData mData =  ParserHelper.getMetaData(sql.toString(), insert.getTableSource());
		mData.setType(SqlType.INSERT);
		List<SQLExpr> columns  = insert.getColumns();
		List<Column> cols = Lists.newArrayList();
		for(int i = 0;i < columns.size();i++) {
			SQLExpr sqlExp = columns.get(i);
			Column column = new Column(mData.getTable(),sqlExp.toString(),"",i,Location.INSERT_COLUMN);
			cols.add(column);
		}
		if(cols.size() == 0) {
			throw new ShardingSqlException(ErrorCodes.UNSUPPORT_SQL);
		}
		mData.setInsertColumns(cols);
		return mData;
	}
	
}
