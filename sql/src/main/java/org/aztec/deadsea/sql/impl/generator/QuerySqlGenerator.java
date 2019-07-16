package org.aztec.deadsea.sql.impl.generator;

import java.util.List;

import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.entity.DatabaseDTO;
import org.aztec.deadsea.common.entity.TableDTO;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.StringUtils;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuerySqlGenerator implements ShardingSqlGenerator {

	@Autowired
	private ShardingConfigurationFactory factory;
	
	public QuerySqlGenerator() {
		// TODO Auto-generated constructor stub
	}

	public String generate(GenerationParameter param) throws DeadSeaException {
		SqlMetaData metaData = param.getSqlMetaData();
		ShardingConfiguration conf = factory.getConfiguration();
		StringBuilder builder = new StringBuilder(metaData.getSourceSql());
		String rawSql = metaData.getSourceSql();
		String newSql = rawSql;
		TableDTO ts = conf.getTargetTable(metaData.getTable());
		if(ts == null) {
			return null;
		}
		DatabaseDTO dbScheme = ts.getDatabase();
		int dbSize = dbScheme.getSize();
		for(int i = 0;i < dbSize;i++) {
			String dbName = dbScheme.getName();
			String newDbName = dbName + "_" + StringUtils.padding(false, "" + i,
					StringUtils.getLeasePadding(dbSize), '0');
			for(int j = 0;j < ts.getSize();j++){
				if(builder.length() == 0) {
					builder.append(" UNION ALL ");
				}
				String tableName = metaData.getTable().name();
				String newTableName = tableName + "_" + StringUtils.padding(false, "" + j,
						StringUtils.getLeasePadding(ts.getSize()), '0');
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

	public String generateSingle(GenerationParameter param) throws ShardingSqlException {
		// TODO Auto-generated method stub
		try {
			return generate(param);
		} catch (DeadSeaException e) {
			return null;
		}
	}

	public List<String> generateMulti(GenerationParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> generateRollback(GenerationParameter param) throws ShardingSqlException {
		// TODO Auto-generated method stub
		return null;
	}

}
