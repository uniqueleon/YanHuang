package org.aztec.deadsea.sql.impl.druid;



import org.aztec.deadsea.sql.ShardingSqlException;

import com.alibaba.druid.sql.ast.SQLStatement;


public interface DruidSqlParser {

	
	public boolean accept(SQLStatement sql);
	public DruidMetaData parse(SQLStatement sql) throws ShardingSqlException;
}
