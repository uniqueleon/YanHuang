package org.aztec.deadsea.common;

import java.util.List;

public interface MetaDataRegister {

	public void regist(MetaData data) throws DeadSeaException;
	public List<MetaData> getRegistedMetaDatas() throws DeadSeaException;
}
