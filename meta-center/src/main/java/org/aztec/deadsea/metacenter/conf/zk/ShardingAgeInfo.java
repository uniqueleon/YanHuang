package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.entity.ShardAgeDTO;

public class ShardingAgeInfo extends ZkConfig {
	
	private Integer no;
	private Long lastValve;
	private Long valve;
	
	public ShardingAgeInfo(String tableName,int ageNo)
			throws IOException, KeeperException, InterruptedException {
		super(tableName + GlobalConst.ZOOKEEPER_PATH_SPLITOR + ageNo, ConfigFormat.JSON);
		// TODO Auto-generated constructor stub
		this.no = ageNo;
	}

	public Long getValve() {
		return valve;
	}

	public void setValve(Long valve) {
		this.valve = valve;
	}


	public MetaData toMetaData() {
		ShardAgeDTO age = new ShardAgeDTO(no,0,valve,lastValve);
		return age;
	}
}
