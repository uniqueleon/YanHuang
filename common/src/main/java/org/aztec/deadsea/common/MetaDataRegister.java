package org.aztec.deadsea.common;

import java.util.List;
import java.util.Map;

public interface MetaDataRegister {

	public Authentication addauth(String username,String password) throws DeadSeaException;
	public Authentication auth(String username,String password) throws DeadSeaException;
	public void regist(Authentication auth,MetaData data) throws DeadSeaException;
	public Map<String,List<MetaData>> getRegistedMetaDatas(Authentication auth) throws DeadSeaException;
}
