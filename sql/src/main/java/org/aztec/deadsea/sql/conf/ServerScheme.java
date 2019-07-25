package org.aztec.deadsea.sql.conf;

import java.util.List;

import org.aztec.deadsea.common.VirtualServer;

public class ServerScheme {
	
	private String host;
	private Integer port;
	private Integer proxyPort;
	private Long[] ranges;
	private Long modulus;
	private boolean virtual;
	private int age;
	private AuthorityScheme authority;
	private List<VirtualServer> nodes;
	
	public ServerScheme() {
		
	}
	
	public ServerScheme(String host, Integer port, Integer proxyPort,  AuthorityScheme authority) {
		super();
		this.host = host;
		this.port = port;
		this.proxyPort = proxyPort;
		this.authority = authority;
	}


	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getHost(){
		return host;
	}
	public Integer getPort() {
		return port;
	}
	public AuthorityScheme getAuthority() {
		return authority;
	}

	public Long[] getRanges() {
		return ranges;
	}

	public void setRanges(Long[] ranges) {
		this.ranges = ranges;
	}

	public Long getModulus() {
		return modulus;
	}

	public void setModulus(Long modulus) {
		this.modulus = modulus;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setAuthority(AuthorityScheme authority) {
		this.authority = authority;
	}

	public List<VirtualServer> getNodes() {
		return nodes;
	}

	public void setNodes(List<VirtualServer> nodes) {
		this.nodes = nodes;
	}
	
	
}
