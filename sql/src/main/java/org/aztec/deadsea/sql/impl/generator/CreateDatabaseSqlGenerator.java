package org.aztec.deadsea.sql.impl.generator;

import java.util.List;

import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.SqlUtils;

import com.google.common.collect.Lists;

public class CreateDatabaseSqlGenerator implements ShardingSqlGenerator {

	public CreateDatabaseSqlGenerator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean accept(GenerationParameter param) {
		// TODO Auto-generated method stub
		return param.getSqlMetaData().getSqlType().equals(SqlType.CREATE_DATABASE);
	}

	@Override
	public String generateSingle(GenerationParameter param) throws ShardingSqlException {

		throw new ShardingSqlException(ShardingSqlException.ErrorCodes.UNSUPPORT_OPERATION);
	}

	@Override
	public List<String> generateMulti(GenerationParameter param) throws ShardingSqlException {
		List<String> retSql = Lists.newArrayList();
		String rawDbName = param.getSqlMetaData().getDatabase().name();
		Integer shardSize = param.getSqlMetaData().getShardSize();
		List<String> shardDbs = SqlUtils.getMultiDatabaseNames(param.getSqlMetaData().getDatabase().name(), param.getSqlMetaData().getShardSize());
		String rawSql = param.getSqlMetaData().getRawSql();
		for(int i = 0;i < shardDbs.size();i++) {
			rawSql.replace(rawDbName, shardDbs.get(i));
		}
		return retSql;
	}


}
