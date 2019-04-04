package org.aztec.deadsea.sql.impl.generator;

import java.util.List;

import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.StringUtils;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.aztec.deadsea.sql.scheme.DatabaseScheme;
import org.aztec.deadsea.sql.scheme.TableScheme;
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
		TableScheme ts = conf.getTargetTable(metaData.getTable());
		DatabaseScheme dbScheme = ts.getDatabase();
		int dbSize = dbScheme.size();
		for(int i = 0;i < dbSize;i++) {
			String dbName = dbScheme.getName();
			String newDbName = dbName + "_" + StringUtils.padding(false, "" + i,
					StringUtils.getLeasePadding(dbSize), '0');
			for(int j = 0;j < ts.size();j++){
				if(builder.length() == 0) {
					builder.append(" UNION ALL ");
				}
				String tableName = metaData.getTable().name();
				String newTableName = tableName + "_" + StringUtils.padding(false, "" + j,
						StringUtils.getLeasePadding(ts.size()), '0');
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

	public String generateSingle(GenerationParameter param) {
		// TODO Auto-generated method stub
		return generate(param);
	}

	public List<String> generateMulti(GenerationParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

}
