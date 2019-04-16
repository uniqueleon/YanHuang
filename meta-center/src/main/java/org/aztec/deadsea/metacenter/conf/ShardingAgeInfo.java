package org.aztec.deadsea.metacenter.conf;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.metacenter.MetaCenterConst;

public class ShardingAgeInfo extends ZkConfig {
	
	private Integer age;
	private Long valve;
	private Long modulus;
	
	public ShardingAgeInfo(String tableName,int ageNo)
			throws IOException, KeeperException, InterruptedException {
		super(MetaCenterConst.ZkConfigPaths.SHARDING_AGE_INFO, ConfigFormat.JSON);
		// TODO Auto-generated constructor stub
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Long getValve() {
		return valve;
	}

	public void setValve(Long valve) {
		this.valve = valve;
	}

	public Long getModulus() {
		return modulus;
	}

	public void setModulus(Long modulus) {
		this.modulus = modulus;
	}

	
}
