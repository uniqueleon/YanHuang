package org.aztec.deadsea.common;

public interface ShardData {

	public DataID getID();
	public <T> T getData();
	public Integer getVersion();
	public VirtualServer getLocation();
}
