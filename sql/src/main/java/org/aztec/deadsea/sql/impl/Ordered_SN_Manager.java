package org.aztec.deadsea.sql.impl;

import java.util.List;

import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.entity.GlobalInfoDTO;
import org.aztec.deadsea.common.entity.TableDTO;
import org.aztec.deadsea.sql.BatchSequenceNumberManager;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingSqlException.ErrorCodes;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.aztec.deadsea.sql.meta.Table;

import com.google.common.collect.Lists;

public class Ordered_SN_Manager implements BatchSequenceNumberManager {

	
	
	public Ordered_SN_Manager() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Long> getSequenceNumbers(SqlMetaData metaData,ShardingConfiguration conf,int aquireBatch) throws DeadSeaException {
		try {
			CacheUtils cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
			
			Table table = metaData.getTable();
			TableDTO ts = conf.getTargetTable(table);
			GlobalInfoDTO globalDto = conf.getGlobal();
			Long batch = globalDto.getInsertBatch();
			List<Long> seqNo = Lists.newArrayList();
			Long tempSeqNo = ts.getRecordSeqNo();
			for(int i = 0;i < batch.intValue();i++) {
				seqNo.add(tempSeqNo + i);
			}
			return seqNo;
		} catch (Exception e) {
			throw new DeadSeaException(e,ErrorCodes.UNKOWN_ERROR);
		}
	}
	@Override
	public boolean releaseSequenceNumber(List<Long> seqNo) {
		// TODO Auto-generated method stub
		return false;
	}

}
