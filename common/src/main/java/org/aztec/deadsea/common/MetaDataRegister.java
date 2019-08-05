package org.aztec.deadsea.common;

import java.util.List;
import java.util.Map;

import org.aztec.deadsea.common.entity.GlobalInfoDTO;

public interface MetaDataRegister {

	public Authentication addauth(String username,String password) throws DeadSeaException;
	public Authentication auth(String username,String password) throws DeadSeaException;
	public void regist(Authentication auth,MetaData data) throws DeadSeaException;
	public boolean exists(Authentication auth,MetaData data) throws DeadSeaException;
	public void remove(Authentication auth,MetaData data) throws DeadSeaException;
	public Map<String,List<MetaData>> getRegistedMetaDatas(Authentication auth) throws DeadSeaException;
	public void update(Authentication auth,MetaData data) throws DeadSeaException;
	public GlobalInfoDTO getGlobalInfo() throws DeadSeaException;

	public interface MetaDataMapKeys {
		public static String GLOBAL_INFORMATION = "globalInfo";
		public static String DATA_BASE_KEY = "db";
		public static String SERVER_AGE = "serverAge";
	}
}
