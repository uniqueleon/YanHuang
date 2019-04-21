package org.aztec.deadsea.common;

public interface Authentication {

	public String getUUID();
	public String getName();
	public String getPassword();
	public boolean isAuthenticated();
}
