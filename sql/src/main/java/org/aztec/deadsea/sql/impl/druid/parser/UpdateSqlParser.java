package org.aztec.deadsea.sql.impl.druid.parser;

import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.impl.druid.DruidSqlParser;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.ast.SQLStatement;

@Component
public class UpdateSqlParser implements DruidSqlParser {

	public UpdateSqlParser() {
		// TODO Auto-generated constructor stub
	}

	public boolean accept(SQLStatement sql) {
		// TODO Auto-generated method stub
		return false;
	}

	public DruidMetaData parse(SQLStatement sql) {
		// TODO Auto-generated method stub
		return null;
	}

}
