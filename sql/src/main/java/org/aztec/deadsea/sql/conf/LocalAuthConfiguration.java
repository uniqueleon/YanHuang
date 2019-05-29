package org.aztec.deadsea.sql.conf;

import org.aztec.autumn.common.utils.BasePropertiesConfig;
import org.aztec.autumn.common.utils.annotation.config.Property;

public class LocalAuthConfiguration extends BasePropertiesConfig{

	@Property("deadsea.meta.auth.username")
	private String username;
	@Property("deadsea.meta.auth.password")
	private String password;
	
	public LocalAuthConfiguration() {
		super("res:/auth.properties");
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

}
