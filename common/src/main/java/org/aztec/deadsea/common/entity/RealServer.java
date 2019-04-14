package org.aztec.deadsea.common.entity;

import java.util.List;

public class RealServer {
	
	private String host;
	private Integer port;

	private RealServerType type;
	private List<VirtualServer> nodes;

	public RealServer() {
		// TODO Auto-generated constructor stub
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
	
	
}
