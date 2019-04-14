package org.aztec.deadsea.common;

import java.util.List;

public interface Router {

	public List<RealServer> route(Route route);
}
