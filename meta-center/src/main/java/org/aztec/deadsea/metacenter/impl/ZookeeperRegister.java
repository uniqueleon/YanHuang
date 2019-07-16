package org.aztec.deadsea.metacenter.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.ServerRegistration;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.common.entity.SimpleAuthentication;
import org.aztec.deadsea.common.entity.SimpleRegistration;
import org.aztec.deadsea.metacenter.MetaDataException;
import org.aztec.deadsea.metacenter.conf.zk.Account;
import org.aztec.deadsea.metacenter.conf.zk.RealServerInfo;
import org.aztec.deadsea.metacenter.conf.zk.ServerAge;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class ZookeeperRegister implements ServerRegister, MetaDataRegister {
	
	private ZkRegistHelper helper;

	public ZookeeperRegister() throws IOException, KeeperException, InterruptedException {
		helper = new ZkRegistHelper();
	}

	public void loadMetaData() {

	}
	
	

	public void regist(Authentication auth,ShardingAge age,List<RealServer> newServers) throws MetaDataException {
		helper.regist(auth, age, newServers);
	}


	public ServerRegistration getRegistration(Authentication auth,ShardingAge age) throws DeadSeaException {
		helper.assertAuth(auth);
		SimpleRegistration registration = new SimpleRegistration();
		ServerAge serverAge = helper.getAge(age.getNo());
		List<RealServerInfo> serverDatas = serverAge.getServers();
		List<RealServer> serverMetaDatas = Lists.newArrayList();
		for(int i = 0;i < serverDatas.size();i++) {
			serverMetaDatas.add(serverDatas.get(i).toDto());
		}
		registration.setAllServers(serverMetaDatas);
		//registration.setCalculator(new D);
		return registration;
	}


	@Override
	public Authentication addauth(String username, String password) throws DeadSeaException {
		Account account = helper.addauth(username, password);
		if(account != null) {
			return new SimpleAuthentication(username, account.getUuid(), password, true);
		}
		return new SimpleAuthentication(username, null, password, false);
	}

	@Override
	public Authentication auth(String username, String password) throws DeadSeaException {
		return helper.auth(username, password);
	}

	@Override
	public void regist(Authentication auth, MetaData data) throws DeadSeaException {
		helper.validateMetaData(auth, data);
		if (auth.isAuthenticated()) {
			switch (data.getType()) {
			case DB:
				switch (data.getType().getSubType()) {
				case DATABASE:
					helper.registDB(auth, data);
					break;
				case TABLE:
					helper.registTable(auth, data);
					break;
				case AGE:
					helper.registAge(auth, data);
					break;
				}
			}
		}
	}


	@Override
	public Map<String, List<MetaData>> getRegistedMetaDatas(Authentication auth) throws DeadSeaException {
		return helper.getRegistedMetaDatas( auth);
	}

	@Override
	public void update(Authentication auth, MetaData data) throws DeadSeaException {
		helper.assertAuth(auth);
		if (auth.isAuthenticated()) {
			switch (data.getType()) {
			case DB:
				switch (data.getType().getSubType()) {
				case DATABASE:

					helper.updateDB(auth, data);
					break;
				case TABLE:
					helper.updateTable(auth, data);
					break;
				case AGE:
					break;
				}
			case CACHE:
				break;
			}
		}
	}

	@Override
	public void update(Authentication auth,ShardingAge age,List<RealServer> newServers) throws DeadSeaException {
		helper.update(auth, age, newServers);
	}

	@Override
	public void remove(Authentication auth, MetaData data) throws DeadSeaException {
		helper.assertAuth(auth);
		if (auth.isAuthenticated()) {
			switch (data.getType()) {
			case DB:
				switch (data.getType().getSubType()) {
				case DATABASE:

					helper.removeDb(auth, data);
					break;
				case TABLE:
					helper.removeTable(auth, data);
					break;
				case AGE:
					break;
				}
			case CACHE:
				break;
			}
		}
	}

	@Override
	public boolean exists(Authentication auth, MetaData data) throws DeadSeaException {
		return helper.exists(auth, data);
	}

}
