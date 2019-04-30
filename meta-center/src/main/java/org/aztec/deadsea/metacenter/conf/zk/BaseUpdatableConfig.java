package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.ZkConfig;

public class BaseUpdatableConfig extends ZkConfig {
	
	private String timestamp;

	public BaseUpdatableConfig(String dataID, ConfigFormat format)
			throws IOException, KeeperException, InterruptedException {
		super(dataID, format);
	}

}
