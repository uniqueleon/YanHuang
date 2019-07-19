package org.aztec.deadsea.sql.impl.druid.parser;

import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.impl.druid.DruidSqlParser;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;

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
		return mData;
	}
	
}
