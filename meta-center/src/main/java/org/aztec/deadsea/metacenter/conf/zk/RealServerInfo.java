package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.entity.RealServerType;
import org.aztec.deadsea.common.entity.SimpleRealServer;
import org.aztec.deadsea.metacenter.MetaCenterConst;

public class RealServerInfo extends ZkConfig {

	private Integer no;
	private String host;
	private Integer port;
	private Integer proxyPort;

	public RealServerInfo(int no)
			throws IOException, KeeperException, InterruptedException {
		super(MetaCenterConst.ZkConfigPaths.REAL_SERVER_INFO + "." + no, ConfigFormat.JSON);
		this.no = no;
	}
	

	public RealServerInfo(int no, String host, Integer port, Integer proxyPort) throws IOException, KeeperException, InterruptedException {
		super(MetaCenterConst.ZkConfigPaths.REAL_SERVER_INFO + "." + no, ConfigFormat.JSON);
		this.host = host;
		this.port = port;
		this.proxyPort = proxyPort;
		this.no = no;
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


	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}


	public Integer getNo() {
		return no;
	}


	public void setNo(Integer no) {
		this.no = no;
	}
	
	public RealServer toDto() {
		SimpleRealServer srs = new SimpleRealServer(host,no,port,RealServerType.SOCKET);
		if(proxyPort != null) {
			srs.setProxyPort(proxyPort);
		}
		return srs;
	}

}
