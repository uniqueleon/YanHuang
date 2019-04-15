package org.aztec.deadsea.metacenter;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.autumn.common.zk.ZkNode;
import org.aztec.autumn.common.zk.ZkConfig.ConfigFormat;

public class ZookeeperRegister {
	
	private ZkConfig baseConfig;

	public ZookeeperRegister() {
		// TODO Auto-generated constructor stub
		
	}

	private void init() throws IOException, KeeperException, InterruptedException {
		baseConfig = new ZkConfig(MetaCenterConst.ZkConfigPaths.BASE_INFO, ConfigFormat.JSON);
		baseConfig = new ZkConfig(MetaCenterConst.ZkConfigPaths.REAL_SERVER_INFO ,ConfigFormat.JSON);
	}
}
