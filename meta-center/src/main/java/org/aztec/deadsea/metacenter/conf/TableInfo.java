package org.aztec.deadsea.metacenter.conf;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.metacenter.MetaCenterConst;

public class TableInfo extends ZkConfig {
	
	private String name;
	private Integer size;

	public TableInfo(int no) throws IOException, KeeperException, InterruptedException {
		super(MetaCenterConst.ZkConfigPaths.TABLES_SHARDING_INFO + "." + no, ConfigFormat.JSON);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
