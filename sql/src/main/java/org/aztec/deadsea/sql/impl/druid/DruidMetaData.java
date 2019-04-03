package org.aztec.deadsea.sql.impl.druid;

import org.aztec.deadsea.sql.impl.BaseSqlMetaData;
import org.aztec.deadsea.sql.meta.SqlMetaData;

public class DruidMetaData extends BaseSqlMetaData implements SqlMetaData {
	
	
	public DruidMetaData(String sql) {
		rawSql = sql;
	}
	
	

}
