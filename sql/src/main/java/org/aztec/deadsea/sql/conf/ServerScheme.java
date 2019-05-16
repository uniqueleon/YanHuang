package org.aztec.deadsea.sql.conf;

public class ServerScheme {
	
	private String host;
	private Integer port;
	private Integer proxyPort;
	private Long[] ranges;
	private Long modulus;
	private boolean virtual;
	private int age;
	private AuthorityScheme authority;
	
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
}
