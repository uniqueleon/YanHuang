package org.aztec.deadsea.common.entity;

import java.util.List;

import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.VirtualServer;

public class SimpleRealServer implements RealServer{
	
	private String host;
	private Integer no;
	private Integer port;
	private Integer proxyPort;
	private RealServerType type;
	private List<VirtualServer> nodes;

	public SimpleRealServer() {
		// TODO Auto-generated constructor stub
	}

	public SimpleRealServer(String host, Integer no, Integer port, RealServerType type) {
		super();
		this.host = host;
		this.no = no;
		this.port = port;
		this.type = type;
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

	public RealServerType getType() {
		return type;
	}

	public void setType(RealServerType type) {
		this.type = type;
	}

	public List<VirtualServer> getNodes() {
		return nodes;
	}

	public void setNodes(List<VirtualServer> nodes) {
		this.nodes = nodes;
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
	
	
}
