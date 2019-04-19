package org.aztec.deadsea.common;

import java.util.List;
import java.util.Map;

public interface Authentication {

	public String getUUID();
	public String getName();
	public String getPassword();
	public Map<String,List<MetaData>> getAuthenticatedMetaDatas();
}
