package org.aztec.deadsea.metacenter.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.ServerRegistration;
import org.aztec.deadsea.common.entity.SimpleAuthentication;
import org.aztec.deadsea.common.entity.SimpleRegistration;
import org.aztec.deadsea.metacenter.MetaDataException;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;
import org.aztec.deadsea.metacenter.conf.zk.Account;
import org.aztec.deadsea.metacenter.conf.zk.BaseInfo;
import org.aztec.deadsea.metacenter.conf.zk.RealServerInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ZookeeperRegister implements ServerRegister, MetaDataRegister {

	private static final BaseInfo baseInfo = BaseInfo.getInstance();

	private static Map<String, Account> accounts = Maps.newConcurrentMap();
	
	private ZkRegistHelper helper = new ZkRegistHelper();

	public ZookeeperRegister() {
		// TODO Auto-generated constructor stub

	}

	public void loadMetaData() {

	}
	
	private void validateRegistServers(List<RealServer> newServers) throws MetaDataException {
		
		for(RealServer server : newServers) {
			if(baseInfo.getRealNum() > server.getNo()) {
				throw new MetaDataException(ErrorCodes.META_DATA_ALREADY_EXISTS);
			}
		}
	}

	public void regist(List<RealServer> newServers) throws MetaDataException {
		validateRegistServers(newServers);
		try {
			if (CollectionUtils.isNotEmpty(newServers)) {

				for (RealServer realServer : newServers) {
					RealServerInfo serverInfo = new RealServerInfo(realServer.getNo(), realServer.getHost(),
							realServer.getPort(), realServer.getProxyPort());
					serverInfo.save();
					baseInfo.setRealNum(baseInfo.getRealNum() + 1);
				}
			}
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}


	public ServerRegistration getRegistration() throws DeadSeaException {
		SimpleRegistration registration = new SimpleRegistration();
		List<RealServerInfo> serverDatas = baseInfo.getServers();
		List<RealServer> serverMetaDatas = Lists.newArrayList();
		for(int i = 0;i < serverDatas.size();i++) {
			serverMetaDatas.add(serverDatas.get(i).toDto());
		}
		registration.setAllServers(serverMetaDatas);
		//registration.setCalculator(new D);
		return registration;
	}

	public List<MetaData> getRegistedMetaDatas() throws DeadSeaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authentication addauth(String username, String password) throws DeadSeaException {

		Account account = helper.addauth(username, password);
		if(account != null) {
			accounts.put(account.getUuid(), account);
			return new SimpleAuthentication(username, account.getUuid(), password, true);
		}
		return new SimpleAuthentication(username, null, password, false);
	}

	@Override
	public Authentication auth(String username, String password) throws DeadSeaException {
		return helper.auth(accounts, username, password);
	}

	@Override
	public void regist(Authentication auth, MetaData data) throws DeadSeaException {
		// TODO Auto-generated method stub

		helper.validateMetaData(auth, data);
		if (auth.isAuthenticated()) {
			switch (data.getType()) {
			case DB:
				switch (data.getType().getSubType()) {
				case DATABASE:
					helper.registDB(accounts,auth, data);
					break;
				case TABLE:
					helper.registTable(accounts,auth, data);
					break;
				case AGE:
					helper.registAge(accounts,auth, data);
					break;
				}
			}
		}
	}


	@Override
	public Map<String, List<MetaData>> getRegistedMetaDatas(Authentication auth) throws DeadSeaException {

		return helper.getRegistedMetaDatas(accounts, auth);
	}

	@Override
	public void update(Authentication auth, MetaData data) throws DeadSeaException {
		// TODO Auto-generated method stub
		helper.assertAuth(auth);
		if (auth.isAuthenticated()) {
			switch (data.getType()) {
			case DB:
				switch (data.getType().getSubType()) {
				case DATABASE:

					helper.updateDB(accounts, auth, data);
					break;
				case TABLE:
					helper.updateTable(accounts, auth, data);
					break;
				case AGE:
					break;
				}
			}
		}
	}

	@Override
	public void update(List<RealServer> newServers) throws DeadSeaException {
		// TODO Auto-generated method stub
		
	}

	private void validateUpdateServers(List<RealServer> newServers) {
		
	}
}
