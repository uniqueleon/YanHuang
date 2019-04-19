package org.aztec.deadsea.common;

import java.util.List;

public interface MetaDataRegister {

	public Authentication addauth(String username,String password) throws DeadSeaException;
	public Authentication auth(String username,String password) throws DeadSeaException;
	public void regist(MetaData data) throws DeadSeaException;
	public List<MetaData> getRegistedMetaDatas() throws DeadSeaException;
}
