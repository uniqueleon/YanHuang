package org.aztec.deadsea.sql.conf;

import java.util.Map;

import com.google.common.collect.Maps;

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
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public AuthorityScheme(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		parameters = Maps.newHashMap();
	}
	
	
}
