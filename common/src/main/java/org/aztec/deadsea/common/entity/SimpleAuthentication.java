package org.aztec.deadsea.common.entity;

import java.util.List;
import java.util.Map;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.MetaData;

public class SimpleAuthentication implements Authentication {
	
	private String username;
	private String uuid;
	private String password;
	private boolean authenticated = false;
	
	public SimpleAuthentication() {
		// TODO Auto-generated constructor stub
	}

	public SimpleAuthentication(String username, String uuid, String password, boolean authenticated) {
		super();
		this.username = username;
		this.uuid = uuid;
		this.password = password;
		this.authenticated = authenticated;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getUuid() {
		return uuid;
	}



	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}



	@Override
	public String getUUID() {
		// TODO Auto-generated method stub
		return uuid;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

}
