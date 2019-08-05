package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.entity.GlobalInfoDTO;
import org.aztec.deadsea.metacenter.MetaCenterConst;
import org.aztec.deadsea.metacenter.MetaCenterLogger;

public class BaseInfo extends ZkConfig {
	
	// 逻辑表数
	private Integer tableNum;
	// 表分片数
	private Integer tableSize;
	// 最大 age
	private Integer maxAge;
	private Long loadServerTimeout;
	private Long loadTableTimeout;
	private Long loadAgesTimeout;
	private Long realNum;
	private Long virtualNum;
	private String globalAccessString;
	private String type;
	private Long insertBatchNum = 10000l;
	//private Integer acquiredBatchNum = 1000;
	private static BaseInfo instance;
	
	static {
		try {
			instance = new BaseInfo();
		} catch (Exception e) {
			e.printStackTrace();
			MetaCenterLogger.error(e);
		}
	}

	private BaseInfo()
			throws IOException, KeeperException, InterruptedException {
		super(MetaCenterConst.ZkConfigPaths.BASE_INFO, ConfigFormat.JSON);
	}
	


	public Integer getTableSize() {
		return tableSize;
	}

	public void setTableSize(Integer tableSize) {
		this.tableSize = tableSize;
	}

	public static BaseInfo getInstance() {
		return instance;
	}



	public Long getRealNum() {
		return realNum;
	}

	public void setRealNum(Long realNum) {
		this.realNum = realNum;
	}

	public Long getVirtualNum() {
		return virtualNum;
	}

	public void setVirtualNum(Long virtualNum) {
		this.virtualNum = virtualNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	

	public Integer getTableNum() {
		return tableNum;
	}

	public void setTableNum(Integer tableNum) {
		this.tableNum = tableNum;
	}

	public Long getLoadServerTimeout() {
		return loadServerTimeout;
	}

	public void setLoadServerTimeout(Long loadServerTimeout) {
		this.loadServerTimeout = loadServerTimeout;
	}

	public Long getLoadTableTimeout() {
		return loadTableTimeout;
	}

	public void setLoadTableTimeout(Long loadTableTimeout) {
		this.loadTableTimeout = loadTableTimeout;
	}

	public Long getLoadAgesTimeout() {
		return loadAgesTimeout;
	}

	public void setLoadAgesTimeout(Long loadAgesTimeout) {
		this.loadAgesTimeout = loadAgesTimeout;
	}

	public String getGlobalAccessString() {
		return globalAccessString;
	}

	public void setGlobalAccessString(String globalAccessString) {
		this.globalAccessString = globalAccessString;
	}
	
	public Long getInsertBatchNum() {
		return insertBatchNum;
	}

	public void setInsertBatchNum(Long insertBatchNum) {
		this.insertBatchNum = insertBatchNum;
	}

	public MetaData toMetaData() {
		GlobalInfoDTO globalInfo = new GlobalInfoDTO(tableNum,maxAge,type,tableSize,globalAccessString);
		globalInfo.setMaxAge(maxAge);
		globalInfo.setInsertBatch(insertBatchNum);
		return globalInfo;
	}
}
