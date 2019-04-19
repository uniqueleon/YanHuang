package org.aztec.deadsea.metacenter.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException;
import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.ServerRegistration;
import org.aztec.deadsea.common.impl.BaseDBCalculator;
import org.aztec.deadsea.common.MetaData.MetaType;
import org.aztec.deadsea.metacenter.MetaDataException;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;
import org.aztec.deadsea.metacenter.conf.zk.BaseInfo;
import org.aztec.deadsea.metacenter.conf.zk.RealServerInfo;
import org.aztec.deadsea.metacenter.conf.zk.ShardingAgeInfo;
import org.aztec.deadsea.metacenter.conf.zk.TableInfo;

import com.google.common.collect.Lists;

public class ZookeeperRegister implements ServerRegister, MetaDataRegister {

	private static final BaseInfo baseInfo = BaseInfo.getInstance();

	public ZookeeperRegister() {
		// TODO Auto-generated constructor stub

	}

	public void loadMetaData() {

	}

	public void regist(List<RealServer> newServers) {
		if(CollectionUtils.isNotEmpty(newServers)) {

			for(RealServer realServer : newServers) {
				//RealServerInfo serverInfo = new RealServerInfo(realServer.getNo(), realServer.getHost(), realServer.getPort(), realServer.getProxyPort(), null, null);
			}
		}
	}

	public void regist(MetaData data) throws DeadSeaException {
		// TODO Auto-generated method stub

		if (!baseInfo.getType().equalsIgnoreCase(data.getType().name())) {
			throw new MetaDataException(ErrorCodes.INCOMPATIBLE_SERVER_TYPE);
		}
		try {
			if (data.getType().equals(MetaType.DB)) {
				int curTableNum = baseInfo.getTableNum();
				curTableNum++;
				TableInfo table = new TableInfo(curTableNum, data.getName(), data.getSize(), data.shard(),
						new ArrayList<ShardingAgeInfo>());
				//baseInfo.registTable(table);
			} else {

			}
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}

	public ServerRegistration getRegistration() throws DeadSeaException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MetaData> getRegistedMetaDatas() throws DeadSeaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authentication addauth(String username, String password) throws DeadSeaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authentication auth(String username, String password) throws DeadSeaException {
		// TODO Auto-generated method stub
		return null;
	}

}
