package org.aztec.deadsea.sql.impl.generator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aztec.deadsea.sql.Asserts;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlException.ErrorCodes;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.SqlUtils;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.aztec.deadsea.sql.scheme.DatabaseScheme;
import org.aztec.deadsea.sql.scheme.TableScheme;

import com.google.common.collect.Lists;

public class CreateTableGenerator implements ShardingSqlGenerator {

	private static Pattern tableNamePattern = Pattern
			.compile("[C|c][R|r][E|e][A|a][T|t][E|e]\\s+[T|t][A|a][B|b][L|l][E|e]\\s+[\\.*\\`*\\w+[_+\\w+]+\\`*]+\\s+\\(");
			//.compile("[C|c][R|r][E|e][A|a][T|t][E|e]\\s*[T|t][A|a][B|b][L|l][E|e]");

	public CreateTableGenerator() {
		// TODO Auto-generated constructor stub
	}

	public boolean accept(GenerationParameter param) {
		// TODO Auto-generated method stub
		return param.getSqlMetaData().getSqlType().equals(SqlType.CREAT_TABLE);
	}

	public String generate(GenerationParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	public String generateSingle(GenerationParameter param) throws ShardingSqlException {
		throw new ShardingSqlException(ShardingSqlException.ErrorCodes.UNSUPPORT_OPERATION);
		// return null;
	}

	public List<String> generateMulti(GenerationParameter param) throws ShardingSqlException {
		List<String> retString = Lists.newArrayList();
		String rawSql = param.getSqlMetaData().getRawSql();
		SqlMetaData metaData = param.getSqlMetaData();
		TableScheme ts = param.getShardingConf().getTargetTable(metaData.getTable());
		Asserts.assertNotNull(ts, ErrorCodes.NO_TABLE_SCHEME_FOUND);
		Asserts.assertNotNull(ts.getDatabase(), ErrorCodes.NO_DATABASE_SCHEME_FOUND);
		DatabaseScheme db = ts.getDatabase();
		List<String> dbTables = SqlUtils.getMultiDatabaseTableNames(db.size(),ts.size(),db.getName(),ts.getName());
		String replaceTarget = getReplaceTarget(rawSql);
		for(int i = 0;i < dbTables.size();i++) {
			retString.add(rawSql.replace(replaceTarget, dbTables.get(i)));
		}
		return retString;
	}
	
	public static String getReplaceTarget(String rawSql) {
		Matcher match = tableNamePattern.matcher(rawSql);
		String retTarget = null;
		if(match.find()) {
			String founded = match.group();
			retTarget = founded.split(" ")[2].trim();
		}
		return retTarget;
	}

	public static void main(String[] args) {
		String sql = "create table `lm_db`.`lm_es_ook` (dsf)";
		
		System.out.println(getReplaceTarget(sql));
	}
}
