package org.aztec.deadsea.sql.impl.druid;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Singleton;

import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.entity.TableDTO;
import org.aztec.deadsea.sql.ShardSqlDialect;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;

@Component()
@Singleton
public class DruidMetaDataBuilder {

	
	@Autowired
	private List<DruidSqlParser> parsers;
	
	@Autowired
	ShardingConfigurationFactory confFactory;
	
	private final static Pattern shardSqlPattern = Pattern.compile(ShardSqlDialect.SHARD_KEY_WORD_PATTERN);
	
	private DruidMetaDataBuilder() {
	}

	
	public DruidMetaData getMetaData(String sql) throws DeadSeaException {
		ShardingConfiguration conf = confFactory.getConfiguration();
		DruidMetaData tmpMetaData = checkShardDialect(sql);
		MySqlStatementParser parser = new MySqlStatementParser(tmpMetaData != null ? 
				tmpMetaData.getSourceSql() : sql);
		SQLStatement statement = parser.parseStatement();
		if(parsers != null && parsers.size() > 0) {
			for(DruidSqlParser sqlParser : parsers) {
				if(sqlParser.accept(statement)) {
					DruidMetaData metaData =  sqlParser.parse(statement);
					if(tmpMetaData != null) {

						metaData.setShard(tmpMetaData.shard());
						metaData.setShardSize(tmpMetaData.getShardSize());
						metaData.setRawSql(tmpMetaData.getRawSql());
						metaData.setSourceSql(tmpMetaData.getSourceSql());
					}
					else {
						checkShardConfig(conf,metaData);
					}
					return metaData;
				}
			}
		}
		return null;
	}
	
	private void checkShardConfig(ShardingConfiguration conf,DruidMetaData metaData) throws DeadSeaException {
		if(metaData.getTable() != null) {
			TableDTO tScheme = conf.getTargetTable(metaData.getTable());
			metaData.setShard(tScheme.shard());
			metaData.setShardSize(tScheme.getSize());
		}
	}
	
	private DruidMetaData checkShardDialect(String rawSql) {
		Matcher matcher = shardSqlPattern.matcher(rawSql);
		if(matcher.find()) {
			DruidMetaData metaData = new DruidMetaData(rawSql);
			String shardPart = matcher.group();
			String shardKeyWord = shardPart.split("\\s+")[1];
			metaData.setShard(true);
			Integer shardSize = Integer.parseInt(shardKeyWord.substring(shardKeyWord.indexOf('(') + 1, 
					shardKeyWord.indexOf(')')));
			metaData.setShardSize(shardSize);
			metaData.setRawSql(rawSql);
			rawSql = rawSql.replace(shardKeyWord, "");
			metaData.setSourceSql(rawSql);
			return metaData;
		}
		return null;
	}
	
}
