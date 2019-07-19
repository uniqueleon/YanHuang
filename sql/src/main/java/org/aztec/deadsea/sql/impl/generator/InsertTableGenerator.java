package org.aztec.deadsea.sql.impl.generator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.entity.DatabaseDTO;
import org.aztec.deadsea.common.entity.TableDTO;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlException.ErrorCodes;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.SqlUtils;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.aztec.deadsea.sql.meta.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class InsertTableGenerator implements ShardingSqlGenerator {
	
	@Autowired
	private ShardingConfigurationFactory factory;

	private static Pattern tableNamePattern = Pattern
			.compile("[I|i][N|n][S|s][E|e][R|r][T|t]\\s+[I|i][N|n][T|t][O|o]\\s+[\\.*\\`*\\w+[_+\\w+]+\\`*]+\\s+\\[V|v][A|a][L|l][U|u][E|e]");
			//.compile("[C|c][R|r][E|e][A|a][T|t][E|e]\\s*[T|t][A|a][B|b][L|l][E|e]");

	public InsertTableGenerator() {
		// TODO Auto-generated constructor stub
	}

	public boolean accept(GenerationParameter param) {
		// TODO Auto-generated method stub
		return param.getSqlMetaData().getSqlType().equals(SqlType.INSERT);
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
		ShardingConfiguration conf = factory.getConfiguration();
		List<String> retString = Lists.newArrayList();
		try {
			String rawSql = param.getSqlMetaData().getSourceSql();
			SqlMetaData metaData = param.getSqlMetaData();
			Table table = metaData.getTable();
			DatabaseDTO db = conf.getDatabaseScheme(param.getSqlMetaData().getDatabase());
			TableDTO ts = new TableDTO(0,table.name(), 
					param.getSqlMetaData().getShardSize(), param.getSqlMetaData().shard(), db);
			List<String> dbTables = SqlUtils.getMultiDatabaseTableNames(db.getSize(),ts.getSize(),db.getName(),ts.getName());
			String replaceTarget = getReplaceTarget(rawSql);
			for(int i = 0;i < dbTables.size();i++) {
				retString.add(rawSql.replace(replaceTarget, dbTables.get(i)));
			}
		} catch (DeadSeaException e) {
			throw new ShardingSqlException(ErrorCodes.UNSUPPORT_OPERATION);
		}
		return retString;
	}
	
	public static String getReplaceTarget(String rawSql) {
		Matcher match = tableNamePattern.matcher(rawSql);
		String retTarget = null;
		if(match.find()) {
			String founded = match.group();
			retTarget = founded.split(" ")[3].trim();
		}
		return retTarget;
	}

	public static void main(String[] args) {
		String sql = "create table `lm_db`.`lm_es_ook` (dsf)";
		
		System.out.println(getReplaceTarget(sql));
	}

	@Override
	public List<String> generateRollback(GenerationParameter param) throws ShardingSqlException {
		ShardingConfiguration conf = factory.getConfiguration();
		List<String> retString = Lists.newArrayList();
		try {
			String rawSql = "DROP TABLE IF EXISTS _TB_";
			SqlMetaData metaData = param.getSqlMetaData();
			Table table = metaData.getTable();
			DatabaseDTO db = conf.getDatabaseScheme(param.getSqlMetaData().getDatabase());
			TableDTO ts = new TableDTO(0,table.name(), 
					param.getSqlMetaData().getShardSize(), param.getSqlMetaData().shard(), db);
			List<String> dbTables = SqlUtils.getMultiDatabaseTableNames(db.getSize(),ts.getSize(),db.getName(),ts.getName());
			String replaceTarget = "_TB_";
			for(int i = 0;i < dbTables.size();i++) {
				retString.add(rawSql.replace(replaceTarget, dbTables.get(i)));
			}
		} catch (DeadSeaException e) {
			throw new ShardingSqlException(ErrorCodes.UNSUPPORT_OPERATION);
		}
		return retString;
	}
}
