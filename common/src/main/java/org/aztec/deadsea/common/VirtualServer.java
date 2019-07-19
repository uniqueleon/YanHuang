package org.aztec.deadsea.common;

import java.util.List;

public interface VirtualServer {

	public RealServer getLocation();
	public void setLocation(RealServer server);
	public List<ShardData> route(RoutingInfo route);
	public <T> T cast(Class<T> castClass);
}
