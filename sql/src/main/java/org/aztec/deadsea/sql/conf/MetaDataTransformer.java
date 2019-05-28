package org.aztec.deadsea.sql.conf;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.entity.DatabaseDTO;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;

import com.google.common.collect.Lists;

public class MetaDataTransformer {
	
	private MetaDataRegister register;
	

	public MetaDataTransformer() {
	}
	

	public static MetaData transfer(Authentication auth,ShardingConfiguration conf, GenerationParameter genParam) {

		switch (genParam.getSqlMetaData().getSqlType()) {
		case CREATE_DATABASE:
			DatabaseDTO dbDto = new DatabaseDTO(0, genParam.getSqlMetaData().getDatabase().name(),
					genParam.getSqlMetaData().getShardSize(), 0, true, Lists.newArrayList());
			return dbDto;
		}
		return null;
	}

}
