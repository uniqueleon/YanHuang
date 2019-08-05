package org.aztec.deadsea.sql;

import java.util.List;

import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.sql.meta.SqlMetaData;

public interface BatchSequenceNumberManager {

	public List<Long> getSequenceNumbers(SqlMetaData metaData,ShardingConfiguration conf,int aquireBatch)throws DeadSeaException ;
	public boolean releaseSequenceNumber(List<Long> seqNo);
}
