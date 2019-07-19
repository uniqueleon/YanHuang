package org.aztec.deadsea.common.entity;

import org.aztec.deadsea.common.DataID;
import org.aztec.deadsea.common.ShardData;
import org.aztec.deadsea.common.VirtualServer;

public class ShardDataImpl implements ShardData {
	
	private DataID id;
	private Object data;
	private VirtualServer server;
	private Integer version;

	public ShardDataImpl(DataID id,Object data) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public DataID getID() {
		return id;
	}

	@Override
	public <T> T getData() {
		return (T) data;
	}

	@Override
	public Integer getVersion() {
		return version;
	}

	@Override
	public VirtualServer getLocation() {
		return server;
	}

}
