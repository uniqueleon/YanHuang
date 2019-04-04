package org.aztec.deadsea.sql.scheme;

import java.util.Map;

public class AuthorityScheme {
	
	private String username;
	private String password;
	private Map<String,Object> parameters;

	public String getUsername(){
		return username;
	}
	public String getPassword() {
		return password;
	}
	public Map<String,Object> getAccessParameters(){
		return parameters;
	}
}
