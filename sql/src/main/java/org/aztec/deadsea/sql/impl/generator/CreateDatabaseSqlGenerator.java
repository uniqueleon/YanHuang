package org.aztec.deadsea.sql.impl.generator;

import java.util.List;

import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.SqlUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
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
		List<String> shardDbs = SqlUtils.getMultiDatabaseNames(rawDbName, shardSize);
		String rawSql = param.getSqlMetaData().getSourceSql();
		for(int i = 0;i < shardDbs.size();i++) {
			retSql.add(rawSql.replace(rawDbName, shardDbs.get(i)));
		}
		return retSql;
	}

	@Override
	public List<String> generateRollback(GenerationParameter param) throws ShardingSqlException {
		String rollBackSqlTmpl = "DROP DATABASE _DB_;";
		List<String> retSql = Lists.newArrayList();
		String rawDbName = "_DB_";
		Integer shardSize = param.getSqlMetaData().getShardSize();
		List<String> shardDbs = SqlUtils.getMultiDatabaseNames(rawDbName, shardSize);
		String rawSql = rollBackSqlTmpl;
		for(int i = 0;i < shardDbs.size();i++) {
			retSql.add(rawSql.replace(rawDbName, shardDbs.get(i)));
		}
		return retSql;
	}


}
