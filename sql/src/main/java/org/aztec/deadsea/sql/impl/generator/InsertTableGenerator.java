package org.aztec.deadsea.sql.impl.generator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aztec.autumn.common.utils.StringUtils;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.DeadSeaLogger;
import org.aztec.deadsea.common.entity.DatabaseDTO;
import org.aztec.deadsea.common.entity.TableDTO;
import org.aztec.deadsea.sql.BatchSequenceNumberManager;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.ShardingSqlException.ErrorCodes;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.impl.druid.DruidMetaData;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.aztec.deadsea.sql.meta.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class InsertTableGenerator implements ShardingSqlGenerator {
	
	@Autowired
	private ShardingConfigurationFactory factory;
	@Autowired
	private BatchSequenceNumberManager snManager;

	private static Pattern tableNamePattern = Pattern
			.compile("[I|i][N|n][S|s][E|e][R|r][T|t]\\s+[I|i][N|n][T|t][O|o]\\s+[\\.*\\`*\\w+[_+\\w+]+\\`*]+");
			//.compile("[C|c][R|r][E|e][A|a][T|t][E|e]\\s*[T|t][A|a][B|b][L|l][E|e]");
	
	private static Pattern insertColumnPattern = Pattern
			.compile("[v|V][a|A][L|l][U|u][e|E][s|S]\\s*\\(");

	public InsertTableGenerator() {
		// TODO Auto-generated constructor stub
	}

	public boolean accept(GenerationParameter param) {
		// TODO Auto-generated method stub
		return param.getSqlMetaData().getSqlType().equals(SqlType.INSERT);
	}

	public String generate(GenerationParameter param) throws ShardingSqlException {
		// TODO Auto-generated method stub
		return generateSingle(param);
	}

	public String generateSingle(GenerationParameter param) throws ShardingSqlException {
		try {
			ShardingConfiguration conf = factory.getConfiguration();
			String rawSql = param.getSqlMetaData().getRawSql();
			SqlMetaData metaData = param.getSqlMetaData();
			DruidMetaData dMetaData = (DruidMetaData) metaData;
			Table table = metaData.getTable();
			DatabaseDTO db = conf.getDatabaseScheme(param.getSqlMetaData().getDatabase());
			TableDTO ts = conf.getTargetTable(table);
			Long seqNo = ts.getRecordSeqNo() == null ? 1 : ts.getRecordSeqNo() + 1;
			Long dbRemainder = seqNo % db.getSize();
			Long tableRemainder = seqNo % ts.getSize();
			String dbName = metaData.getDatabase().name();
			String tableName = metaData.getTable().name();
			int leasePadding = StringUtils.getLeasePadding(ts.getSize());
			String replaceStr = "`" + dbName + "_" + StringUtils.padding(true, "" + dbRemainder, leasePadding, '0') + "`";
			replaceStr += "." + "`" + tableName + "_" + StringUtils.padding(true, "" + tableRemainder, leasePadding, '0') + "`";
			String replaceTarget = getReplaceTarget(rawSql);
			String newSql = injectId(rawSql, replaceTarget, seqNo);
			newSql = newSql.replace(replaceTarget,replaceStr);
			dMetaData.setSequenceNo(seqNo);
			List<Long> targetIds  = Lists.newArrayList();
			targetIds.add(seqNo);
			dMetaData.setTargetIds(targetIds);
			return newSql;
		} catch (DeadSeaException e) {
			throw new ShardingSqlException(ErrorCodes.UNKOWN_ERROR);
		}
		
	}
	
	public String generateSingle(GenerationParameter param,Long seqNo) throws ShardingSqlException {
		try {
			ShardingConfiguration conf = factory.getConfiguration();
			String rawSql = param.getSqlMetaData().getRawSql();
			SqlMetaData metaData = param.getSqlMetaData();
			DruidMetaData dMetaData = (DruidMetaData) metaData;
			Table table = metaData.getTable();
			DatabaseDTO db = conf.getDatabaseScheme(param.getSqlMetaData().getDatabase());
			TableDTO ts = conf.getTargetTable(table);
			Long dbRemainder = seqNo % db.getSize();
			Long tableRemainder = seqNo % ts.getSize();
			String dbName = metaData.getDatabase().name();
			String tableName = metaData.getTable().name();
			int leasePadding = StringUtils.getLeasePadding(ts.getSize());
			String replaceStr = "`" + dbName + "_" + StringUtils.padding(true, "" + dbRemainder, leasePadding, '0') + "`";
			replaceStr += "." + "`" + tableName + "_" + StringUtils.padding(true, "" + tableRemainder, leasePadding, '0') + "`";
			String replaceTarget = getReplaceTarget(rawSql);
			String newSql = injectId(rawSql, replaceTarget, seqNo);
			newSql = newSql.replace(replaceTarget,replaceStr);
			dMetaData.setSequenceNo(seqNo);
			List<Long> targetIds  = Lists.newArrayList();
			targetIds.add(seqNo);
			dMetaData.setTargetIds(targetIds);
			return newSql;
		} catch (DeadSeaException e) {
			throw new ShardingSqlException(ErrorCodes.UNKOWN_ERROR);
		}
		
	}
	
	public int getInsertBatch() {
		return 1;
	}

	public List<String> generateMulti(GenerationParameter param) throws ShardingSqlException {
		List<String> retString = Lists.newArrayList();

		try {
			ShardingConfiguration conf = factory.getConfiguration();
			List<Long> seqNo = snManager.getSequenceNumbers(param.getSqlMetaData(), conf,getInsertBatch());
			for (int i = 0; i < seqNo.size(); i++) {
				retString.add(generateSingle(param, seqNo.get(i)));
			}
 
		} catch (Exception e) {
			DeadSeaLogger.error(e.getMessage(), e);
			throw new ShardingSqlException(ErrorCodes.UNSUPPORT_OPERATION);
		}
		return retString;
	}
	
	
	public static String injectId(String rawSql,String replTarget,Long seqNo) {
		int columnIndex = findIdColumnInsertPosition(rawSql, replTarget);
		int valueIndex = findIdValueInsertPosition(rawSql);
		String str1 = rawSql.substring(0,columnIndex);
		String str2 = rawSql.substring(columnIndex,valueIndex);
		String str3 = rawSql.substring(valueIndex,rawSql.length());
		return str1 + "`id`," + str2 + seqNo + "," + str3;
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
	
	public static int findIdColumnInsertPosition(String rawSql,String matchStr) {
		int beginIndex = rawSql.indexOf(matchStr);
		int idColumnIndex = rawSql.indexOf("(", beginIndex + matchStr.length() - 1);
		return idColumnIndex + 1;
	}
	
	public static int findIdValueInsertPosition(String rawSql) {
		Matcher matcher = insertColumnPattern.matcher(rawSql);
		if(matcher.find()){
			String findStr = matcher.group();
			return rawSql.indexOf(findStr) + findStr.length();
		}
		return -1;
	}

	public static void main(String[] args) {
		
		String sql = "insert into `lm_db`.`lm_es_ook` (`name`) values (`targd`)";
		String regex = "`?lm_db`?";
		String replTarget = getReplaceTarget(sql);
		System.out.println(replTarget.replaceAll("`?lm_db`?", "`lms_db`"));
		System.out.println(injectId(sql, replTarget, 10l));
		Matcher matcher = insertColumnPattern.matcher(sql);
	}

	@Override
	public List<String> generateRollback(GenerationParameter param) throws ShardingSqlException {
		ShardingConfiguration conf = factory.getConfiguration();
		List<String> retString = Lists.newArrayList();
		
		return retString;
	}
}
