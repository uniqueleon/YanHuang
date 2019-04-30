package org.aztec.deadsea.common;

import java.util.List;

public interface VirtualServer {

	
	public List<ShardData> find(Route route);
	public <T> T cast(Class<T> castClass);
}
