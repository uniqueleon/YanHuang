package org.aztec.deadsea.sql;

import java.util.List;

import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.sql.meta.SqlMetaData;

public interface BatchSequenceNumberManager {

	public List<Long> getSequenceNumbers(SqlMetaData metaData,ShardingConfiguration conf,int batch)throws DeadSeaException ;
	public Long pickSequenceNumber(SqlMetaData metaData,Long base,ShardingConfiguration conf)throws DeadSeaException ;
	public boolean release(SqlMetaData metaData)throws DeadSeaException ;
	public boolean cancel(SqlMetaData metaData)throws DeadSeaException;
	public boolean commit(SqlMetaData metaData)throws DeadSeaException ;
	public Long getMaxSequenceNumber(SqlMetaData metaData)throws DeadSeaException ;
}
