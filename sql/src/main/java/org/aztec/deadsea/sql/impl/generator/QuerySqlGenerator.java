package org.aztec.deadsea.sql.impl.generator;

import org.aztec.autumn.common.utils.sql.ShardingSqlGenerator.SqlType;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.StringUtils;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.springframework.stereotype.Component;

@Component
public class QuerySqlGenerator implements ShardingSqlGenerator {

	public QuerySqlGenerator() {
		// TODO Auto-generated constructor stub
	}

	public String generate(GenerationParameter param) {
		SqlMetaData metaData = param.getSqlMetaData();
		ShardingConfiguration conf = param.getShardingConf();
		StringBuilder builder = new StringBuilder(metaData.getRawSql());
		String rawSql = metaData.getRawSql();
		String newSql = rawSql;
		int dbSize = conf.getDataBaseNum();
		for(int i = 0;i < dbSize;i++) {
			String dbName = metaData.getDatabase().name();
			String newDbName = dbName + "_" + StringUtils.padding(false, "" + i,
					StringUtils.getLeasePadding(conf.getDataBaseNum()), '0');
			for(int j = 0;j < conf.getTableNum();j++){
				if(builder.length() == 0) {
					builder.append(" UNION ALL ");
				}
				String tableName = metaData.getTable().name();
				String newTableName = tableName + "_" + StringUtils.padding(false, "" + j,
						StringUtils.getLeasePadding(conf.getTableNum()), '0');
				newSql = rawSql.replace(dbName, newDbName);
				newSql = rawSql.replace(tableName, newTableName);
				builder.append(rawSql.replace(tableName, newTableName));
			}
		}
		return builder.toString();
	}

	public boolean accept(GenerationParameter param) {
		return param.getSqlMetaData().getSqlType().equals(SqlType.QUERY);
	}

}
