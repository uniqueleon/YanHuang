package org.aztec.deadsea.sql.impl;

import java.util.List;
import java.util.Random;

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
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class Ordered_SN_Manager implements BatchSequenceNumberManager {

	private static final Random random = new Random();
	private static final String SEQUENCE_NUMBER_RECORD = "SEQUENCE_NUMBER_RECORD";
	private static final String SQL_TEMP_RECORD = "SQL_TEMP_SEQ_NUMBER_";
	
	public Ordered_SN_Manager() {
		// TODO Auto-generated constructor stub
	}
	
	private String getRedisTmpKey(SqlMetaData metaData) {
		return SQL_TEMP_RECORD + metaData.getUUID();
	}
	
	private String getRedisKey(SqlMetaData metaData) {
		String dbName = metaData.getDatabase().name();
		String tableName = metaData.getTable().name();
		String suffix = dbName + "_" + tableName;
		return SEQUENCE_NUMBER_RECORD + "_" + suffix;
	}

	@Override
	public List<Long> getSequenceNumbers(SqlMetaData metaData,ShardingConfiguration conf,int batch) throws DeadSeaException {
		try {
			//cacheUtil.lock(key, timeout);
			Table table = metaData.getTable();
			TableDTO ts = conf.getTargetTable(table);
			List<Long> seqNo = Lists.newArrayList();
			for(int i = 0;i < batch;i++) {
				seqNo.add(pickSequenceNumber(metaData, ts.getRecordSeqNo(), conf));
			}
			return seqNo;
		} catch (Exception e) {
			throw new DeadSeaException(e,ErrorCodes.UNKOWN_ERROR);
		}
	}
	@Override
	public boolean release(SqlMetaData metaData) throws DeadSeaException {
		try {
			String redisKey = getRedisTmpKey(metaData);
			CacheUtils cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
			cacheUtil.remove(redisKey);
			return true;
		} catch (Exception e) {
			throw new DeadSeaException(e,ErrorCodes.UNKOWN_ERROR);
		}
	}

	@Override
	public Long pickSequenceNumber(SqlMetaData metaData,Long base,ShardingConfiguration conf) throws DeadSeaException {

		try {
			String redisKey = getRedisTmpKey(metaData);
			String otherKey = getRedisKey(metaData);
			CacheUtils cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
			GlobalInfoDTO globalDto = conf.getGlobal();
			Long batch = globalDto.getInsertBatch();
			Long startPos = cacheUtil.bitcount(redisKey);
			while(startPos < batch && !cacheUtil.setBitWhileUnset(redisKey, (startPos + 1), true)
					&& cacheUtil.isIntersected(redisKey, otherKey)) {
				startPos = cacheUtil.bitcount(redisKey);
			}
			return startPos;
		} catch (Exception e) {
			throw new DeadSeaException(e,ErrorCodes.UNKOWN_ERROR);
		}
	}

	@Override
	public Long getMaxSequenceNumber(SqlMetaData metaData) throws DeadSeaException {
		try {
			String redisKey = getRedisKey(metaData);
			CacheUtils cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
			List<Long> sequenceNumber = cacheUtil.getAllSetBits(redisKey);
			return sequenceNumber.get(sequenceNumber.size() - 1);
		} catch (Exception e) {
			throw new DeadSeaException(e,ErrorCodes.UNKOWN_ERROR);
		}
	}

	@Override
	public boolean commit(SqlMetaData metaData) throws DeadSeaException {
		try {
			CacheUtils cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
			boolean result =  cacheUtil.mergeBit(getRedisTmpKey(metaData),  getRedisKey(metaData));
			return result;
		}
		catch (Exception e) {
			throw new DeadSeaException(e,ErrorCodes.UNKOWN_ERROR);
		}
	}

	@Override
	public boolean cancel(SqlMetaData metaData) throws DeadSeaException {
		try {
			String redisKey = getRedisTmpKey(metaData);
			String mainKey = getRedisKey(metaData);
			CacheUtils cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
			cacheUtil.substract(mainKey,redisKey);
			cacheUtil.remove(redisKey);
			return true;
		} catch (Exception e) {
			throw new DeadSeaException(e,ErrorCodes.UNKOWN_ERROR);
		}
	}

}
