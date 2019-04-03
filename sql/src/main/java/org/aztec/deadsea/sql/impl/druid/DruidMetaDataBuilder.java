package org.aztec.deadsea.sql.impl.druid;

import java.util.List;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;

@Component()
@Singleton
public class DruidMetaDataBuilder {

	
	@Autowired
	private List<DruidSqlParser> parsers;
	
	private DruidMetaDataBuilder() {
	}

	
	public DruidMetaData getMetaData(String sql) {
		MySqlStatementParser parser = new MySqlStatementParser(sql);
		SQLStatement statement = parser.parseStatement();
		if(parsers != null && parsers.size() > 0) {
			for(DruidSqlParser sqlParser : parsers) {
				if(sqlParser.accept(statement)) {
					return sqlParser.parse(statement);
				}
			}
		}
		return null;
	}
}
