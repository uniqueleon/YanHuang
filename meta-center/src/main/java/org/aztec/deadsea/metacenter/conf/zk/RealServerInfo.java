package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.VirtualServer;
import org.aztec.deadsea.common.entity.ModulusVirtualNode;
import org.aztec.deadsea.common.entity.RealServerType;
import org.aztec.deadsea.common.entity.SimpleRealServer;
import org.aztec.deadsea.metacenter.MetaCenterConst;

public class RealServerInfo extends ZkConfig {

	private Integer no;
	private String host;
	private Integer port;
	private Integer proxyPort;
	private Integer age;
	private Long modulus;
	private Long[] ranges;
	
	public RealServerInfo(Integer no, Integer age)
			throws IOException, KeeperException, InterruptedException {
		super(String.format(MetaCenterConst.ZkConfigPaths.REAL_SERVER_INFO, new Object[] {age,no}), ConfigFormat.JSON);
		this.no = no;
		this.age = age;
	}


	public RealServerInfo(int age,int no, String host, Integer port, Integer proxyPort) throws IOException, KeeperException, InterruptedException {
		super(String.format(MetaCenterConst.ZkConfigPaths.REAL_SERVER_INFO, new Object[] {age,no}), ConfigFormat.JSON);
		this.host = host;
		this.port = port;
		this.proxyPort = proxyPort;
		this.no = no;
		this.age = age;
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

	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public Long getModulus() {
		return modulus;
	}


	public void setModulus(Long modulus) {
		this.modulus = modulus;
	}


	public Long[] getRanges() {
		return ranges;
	}

	public void setRanges(Long[] ranges) {
		this.ranges = ranges;
	}


	public RealServer toDto() {
		SimpleRealServer srs = new SimpleRealServer(age,host,no,port,RealServerType.SOCKET);
		if(proxyPort != null) {
			srs.setProxyPort(proxyPort);
		}
		return srs;
	}
	
	public void setVirtualServerInfo(List<VirtualServer> vServers) {
		if(CollectionUtils.isNotEmpty(vServers)) {
			for(VirtualServer vServer : vServers) {
				if(vServer instanceof ModulusVirtualNode) {
					ModulusVirtualNode mVNode = vServer.cast(ModulusVirtualNode.class);
					if(modulus == null) {
						modulus = mVNode.getModulus();
						ranges = mVNode.getRanges();
					}
				}
			}
		}
	}

}
