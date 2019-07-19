package org.aztec.deadsea.sql.conf;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.entity.DatabaseDTO;
import org.aztec.deadsea.common.entity.TableDTO;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.SqlType;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.aztec.deadsea.sql.meta.Table;

import com.google.common.collect.Lists;

public class MetaDataTransformer {

	private MetaDataRegister register;

	public MetaDataTransformer() {
	}

	public static MetaData transfer(Authentication auth, ShardingConfiguration conf,
			GenerationParameter genParam, boolean isAnti)
			throws DeadSeaException {

		SqlMetaData sqlMetaData = genParam.getSqlMetaData();
		SqlType sqlType = genParam.getSqlMetaData().getSqlType();
		if(isAnti){
			sqlType = sqlType.getAntiType();
		}
		switch (sqlType) {
		case CREATE_DATABASE:
			DatabaseDTO dbDto = new DatabaseDTO(0, sqlMetaData.getDatabase().name().replaceAll("`", ""), sqlMetaData.getShardSize(), 0,
					sqlMetaData.shard(), Lists.newArrayList());
			return dbDto;
		case CREATE_TABLE:
			dbDto = conf.getDatabaseScheme(sqlMetaData.getDatabase());
			Table table = sqlMetaData.getTable();
			TableDTO tableDto = new TableDTO(dbDto.getTableNum(), table.name().replaceAll("`", ""), sqlMetaData.getShardSize(),
					sqlMetaData.shard(), dbDto);
			return tableDto;
		case DROP_TABLE:
			tableDto = conf.getTargetTable(sqlMetaData.getTable());
			return tableDto;
		default:
			break;
		}
		return null;
	}

}
