package org.aztec.deadsea.common;

import java.util.List;

public interface ShardingAge {

	public DataID lastID();
	public List<RealServer> getRealServers();

}
