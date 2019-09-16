package org.aztec.deadsea.sandbox.conf;

import org.aztec.autumn.common.utils.BasePropertiesConfig;
import org.aztec.autumn.common.utils.annotation.config.Property;

import com.baidu.disconf.client.common.annotations.DisconfFileItem;


public class JDBCConfig extends BasePropertiesConfig{
	
	@Property("jdbc.connectionUrl")
	private String connectionUrl = "";
	@Property("jdbc.username")
	private String username= "";
	@Property("jdbc.password")
	private String password = "";

	public JDBCConfig() {
		super("res:/jdbc.properties");
		init();
	}
	
	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static void main(String[] args) {
		JDBCConfig confi = new JDBCConfig();
		System.out.println(confi.getConnectionUrl());
	}
}
