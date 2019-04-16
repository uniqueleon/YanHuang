package org.aztec.deadsea.metacenter.conf;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.metacenter.MetaCenterConst;

public class RealServerInfo extends ZkConfig {

	private String host;
	private Integer port;
	private Long upperLimit;
	private Long lowerLimit;

	public RealServerInfo(int no)
			throws IOException, KeeperException, InterruptedException {
		super(MetaCenterConst.ZkConfigPaths.REAL_SERVER_INFO + "." + no, ConfigFormat.JSON);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Long getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Long upperLimit) {
		this.upperLimit = upperLimit;
	}

	public Long getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Long lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

}
