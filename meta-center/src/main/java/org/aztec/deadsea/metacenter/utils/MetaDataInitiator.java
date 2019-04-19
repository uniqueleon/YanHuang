package org.aztec.deadsea.metacenter.utils;

import org.aztec.deadsea.metacenter.MetaDataException;
import org.aztec.deadsea.metacenter.conf.BaseConfDefaultValues;
import org.aztec.deadsea.metacenter.conf.BaseInfo;
import org.aztec.deadsea.metacenter.conf.RealServerInfo;

public class MetaDataInitiator {

	public MetaDataInitiator() {
		// TODO Auto-generated constructor stub
	}

	public void initDB(String dbName, String host, Integer port, Integer proxyPort) throws MetaDataException {
		try {
			BaseInfo baseInfo = BaseInfo.getInstance();
			baseInfo.setTableNum(BaseConfDefaultValues.TABLE_NUM);
			baseInfo.setRealNum(BaseConfDefaultValues.INIT_SERVER_NUM);
			baseInfo.setVirtualNum(BaseConfDefaultValues.INIT_VIRTUAL_SERVER_NUM);
			baseInfo.setTableSize(BaseConfDefaultValues.TABLE_SIZE);
			baseInfo.setLoadAgesTimeout(BaseConfDefaultValues.LOAD_AGE_TIME_OUT);
			baseInfo.setLoadServerTimeout(BaseConfDefaultValues.LOAD_SERVER_TIME_OUT);
			baseInfo.setLoadTableTimeout(BaseConfDefaultValues.LOAD_TABLE_TIME_OUT);
			baseInfo.setType(BaseConfDefaultValues.SERVER_TYPES[0]);
			baseInfo.setMaxAge(0);
			//baseInfo.save();
			RealServerInfo realServerInfo = new RealServerInfo(0, host, port, proxyPort);
			baseInfo.registServer(realServerInfo);
		} catch (Exception e) {
			throw new MetaDataException(MetaDataException.ErrorCodes.BASE_INFO_INIT_ERROR);
		}
	}

	public static void main(String[] args) {
		try {
			if (args.length < 3) {
				System.err.println(" Should provide 3 parameters at least!");
				System.exit(0);
			}
			MetaDataInitiator initiator = new MetaDataInitiator();
			initiator.initDB(args[0], args[1], Integer.parseInt(args[2]),Integer.parseInt(args[3]));
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
