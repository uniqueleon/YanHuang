package org.aztec.deadsea.sql.conf;

import java.util.List;

public class ServerScheme {
	
	private String host;
	private Integer port;
	private boolean virtual;
	private AuthorityScheme authority;
	private List<DatabaseScheme> databases;
	
	public ServerScheme() {
		
	}
	
	public String getHost(){
		return host;
	}
	public Integer getPort() {
		return port;
	}
	public boolean isVirtual() {
		return virtual;
	}
	public AuthorityScheme getAuthority() {
		return authority;
	}
}
