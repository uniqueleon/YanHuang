package org.aztec.deadsea.metacenter.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.utils.security.CodeCipher;
import org.aztec.autumn.common.zk.ZkUtils;
import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegistration;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.common.entity.DatabaseDTO;
import org.aztec.deadsea.common.entity.GlobalInfoDTO;
import org.aztec.deadsea.common.entity.ShardAgeDTO;
import org.aztec.deadsea.common.entity.SimpleAuthentication;
import org.aztec.deadsea.common.entity.SimpleRegistration;
import org.aztec.deadsea.common.entity.TableDTO;
import org.aztec.deadsea.common.impl.ModerateScaler;
import org.aztec.deadsea.metacenter.MetaCenterConst;
import org.aztec.deadsea.metacenter.MetaCenterLogger;
import org.aztec.deadsea.metacenter.MetaDataException;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;
import org.aztec.deadsea.metacenter.conf.zk.Account;
import org.aztec.deadsea.metacenter.conf.zk.BaseInfo;
import org.aztec.deadsea.metacenter.conf.zk.DatabaseInfo;
import org.aztec.deadsea.metacenter.conf.zk.RealServerInfo;
import org.aztec.deadsea.metacenter.conf.zk.ServerAge;
import org.aztec.deadsea.metacenter.conf.zk.ShardingAgeInfo;
import org.aztec.deadsea.metacenter.conf.zk.TableInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ZkRegistHelper {

	private CodeCipher cipher = new CodeCipher();

	private static final Map<String, Account> accounts = Maps.newConcurrentMap();

	private static final Map<Integer, ServerAge> ages = Maps.newConcurrentMap();

	private static final BaseInfo baseInfo = BaseInfo.getInstance();
	
	private static final ModerateScaler scaler = null;
	
	

	public ZkRegistHelper() throws Exception {
		initData();
	}

	private void initData() throws Exception {
		if (baseInfo.getMaxAge() != null) {
			for (int i = 0; i <= baseInfo.getMaxAge(); i++) {
				ServerAge age = new ServerAge(i);
				if (!age.isDeprecated()) {
					ages.put(age.getAge(), age);
				}
			}
		}
	}

	public void updateDB(Authentication auth, MetaData data) throws MetaDataException {
		try {
			Account account = getAccountInfo(auth);
			DatabaseDTO db = data.cast();
			DatabaseInfo dbInfo = account.findDatabaseInfo(data);
			if (data.getName() != null) {
				dbInfo.setName(data.getName());
			}
			if (data.getSize() != null) {
				dbInfo.setSize(data.getSize());
			}
			if (data.shard() != null) {
				dbInfo.setShard(data.shard());
			}
			if (db.getTableNum() != null) {
				dbInfo.setTableNum(db.getTableNum());
			}
			dbInfo.save();
			// only to triger server synchorization!
			account.save();
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}

	public Account getAccountInfo(Authentication auth) throws Exception {

		Account account = accounts.get(auth.getUUID());
		// account.refresh();
		return account;
	}

	public void updateTable(Authentication auth, MetaData data) throws MetaDataException {

		try {
			Account account = getAccountInfo(auth);
			TableDTO tbData = data.cast();
			TableInfo table = account.findTableInfo(data);
			if (data.getName() != null) {
				table.setName(data.getName());
			}
			if (table.getSize() != null) {
				table.setSize(data.getSize());
			}
			if (data.shard() != null) {
				table.setShard(data.shard());
			}
			table.setAgeNum(tbData.getAgeNum());
			table.setRecordSeqNo(tbData.getRecordSeqNo());
			table.save();
			account.refresh();
		} catch (Exception e) {

			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}

	public void registAge(Authentication auth, MetaData data) throws MetaDataException {

		String tablePrefix = String.format(MetaCenterConst.ZkConfigPaths.SHARDING_AGE_INFO,
				new Object[] { auth.getUUID(), data.getParent().getParent().getSeqNo(), data.getParent().getSeqNo() });

		try {
			TableInfo table = accounts.get(auth.getUUID()).getDatabases().get(data.getParent().getParent().getSeqNo())
					.getTables().get(data.getParent().getSeqNo());
			ShardingAgeInfo ageInfo = new ShardingAgeInfo(tablePrefix, data.getSeqNo());
			ShardAgeDTO sAge = data.cast();
			ageInfo.setValve(sAge.getValve());
			ageInfo.save();
			table.setAgeNum(table.getAgeNum() + 1);
			table.save();
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}

	public void validateMetaData(Authentication auth, MetaData data) throws MetaDataException {

		assertAuth(auth);
		switch (data.getType().getSubType()) {
		case DATABASE:
			DatabaseDTO db = data.cast();
			String path = String.format(MetaCenterConst.ZkConfigPaths.DATABASE_INFO,
					new Object[] { auth.getUUID(), db.getSeqNo() });
			if (ZkUtils.isNodeExists(path)) {
				throw new MetaDataException(ErrorCodes.META_DATA_ALREADY_EXISTS);
			}
			break;
		case TABLE:
			String dbPrefix = String.format(MetaCenterConst.ZkConfigPaths.DATABASE_INFO,
					new Object[] { auth.getUUID(), data.getParent().getSeqNo() });
			if (!ZkUtils.isNodeExists(dbPrefix)) {
				throw new MetaDataException(ErrorCodes.META_DATA_NOT_EXISTS);
			}
			MetaData parent = data.getParent();
			String tablePath = String.format(MetaCenterConst.ZkConfigPaths.TABLES_SHARDING_INFO,
					new Object[] { auth.getUUID(), parent.getSeqNo(), data.getSeqNo() });
			if (ZkUtils.isNodeExists(tablePath)) {
				throw new MetaDataException(ErrorCodes.META_DATA_ALREADY_EXISTS);
			}
			break;
		case AGE:
			String tablePrefix = String.format(MetaCenterConst.ZkConfigPaths.SHARDING_AGE_INFO, new Object[] {
					auth.getUUID(), data.getParent().getParent().getSeqNo(), data.getParent().getSeqNo() });
			if (!ZkUtils.isNodeExists(tablePrefix)) {
				throw new MetaDataException(ErrorCodes.META_DATA_NOT_EXISTS);
			}
			break;
		}
	}

	public void registDB(Authentication auth, MetaData data) throws MetaDataException {

		try {
			Account account = getAccountInfo(auth);

			DatabaseInfo dbInfo = new DatabaseInfo(account.getDataID(), account.getDbNum());
			dbInfo.setName(data.getName());
			dbInfo.setSize(data.getSize());
			dbInfo.setShard(data.shard());
			dbInfo.setTableNum(0);
			dbInfo.setNo(data.getSeqNo());
			dbInfo.save();
			account.refresh();
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}

	public void registTable(Authentication auth, MetaData data) throws MetaDataException {

		String dbPrefix = String.format(MetaCenterConst.ZkConfigPaths.DATABASE_INFO,
				new Object[] { auth.getUUID(), data.getParent().getSeqNo() });
		try {
			Account account = getAccountInfo(auth);
			TableInfo table = new TableInfo(dbPrefix, data.getSeqNo());
			table.setName(data.getName());
			table.setSize(data.getSize());
			table.setShard(data.shard());
			table.setAges(Lists.newArrayList());
			table.setNo(data.getSeqNo());
			table.setAgeNum(0);
			table.setRecordSeqNo(0l);
			table.save();
			DatabaseInfo db = account.findDatabaseInfo(data.getParent());
			db.refresh();
		} catch (Exception e) {

			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}

	public void assertAuth(Authentication auth) throws MetaDataException {
		if (!auth.isAuthenticated()) {
			throw new MetaDataException(ErrorCodes.NOT_AUTHORIZED);
		}
	}

	public Map<String, List<MetaData>> getRegistedMetaDatas(Authentication auth) throws DeadSeaException {
		assertAuth(auth);
		Map<String, List<MetaData>> dataMap = Maps.newHashMap();
		List<MetaData> ageList = Lists.newArrayList();
		for (int i = 0; i < ages.size(); i++) {
			ageList.add(ages.get(i).toMetaData());
		}
		dataMap.put(MetaDataRegister.MetaDataMapKeys.SERVER_AGE, ageList);
		List<MetaData> globalInfo = Lists.newArrayList();
		globalInfo.add(baseInfo.toMetaData());
		dataMap.put(MetaDataRegister.MetaDataMapKeys.GLOBAL_INFORMATION, globalInfo);
		String base64 = cipher.encodeBase64(auth.getName());
		Account account = accounts.get(base64);
		List<DatabaseInfo> databases = account.getDatabases();
		List<MetaData> dbs = Lists.newArrayList();
		if (databases != null) {
			for (DatabaseInfo dbInfo : databases) {
				dbs.add(dbInfo.toMetaData());
			}
		}
		dataMap.put(MetaDataRegister.MetaDataMapKeys.DATA_BASE_KEY, dbs);
		return dataMap;
	}

	public Authentication auth(String username, String password) throws DeadSeaException {
		String base64 = cipher.encodeBase64(username);
		String node = String.format(MetaCenterConst.ZkConfigPaths.BASE_AUTHENTICATIONS_INFO, new Object[] { base64 });
		Account account = accounts.get(base64);
		try {
			if (account == null && ZkUtils.isNodeExists(node)) {
				account = new Account(base64);
				accounts.put(base64, account);
			}
			if (account != null) {
				String pwd = cipher.getMD5Substract(password, GlobalConst.DEFAULT_CHARSET);
				if (account.getPassword().equals(pwd)) {
					return new SimpleAuthentication(username, base64, password, true);
				} else {
					return new SimpleAuthentication(username, base64, password, false);
				}
			} else {
				throw new MetaDataException(ErrorCodes.AUTHENTICATE_FAIL);
			}
		} catch (Exception e) {
			MetaCenterLogger.error(e);
			throw new MetaDataException(ErrorCodes.AUTHENTICATE_FAIL);
		}
	}

	public Account addauth(String username, String password) throws DeadSeaException {

		String base64 = cipher.encodeBase64(username);
		String node = String.format(MetaCenterConst.ZkConfigPaths.BASE_AUTHENTICATIONS_INFO, new Object[] { base64 });
		if (!ZkUtils.isNodeExists(node)) {
			try {
				Account account = new Account(base64);
				account.setUuid(UUID.randomUUID().toString());
				account.setDbNum(0);
				account.setDatabases(Lists.newArrayList());
				account.setUsername(username);
				account.setPassword(cipher.getMD5Substract(password, GlobalConst.DEFAULT_CHARSET));
				account.save();
				accounts.put(account.getUuid(), account);
				return account;
			} catch (Exception e) {
				MetaCenterLogger.error(e);
				return null;
			}

		} else {
			throw new MetaDataException(ErrorCodes.AUTHENTICATION_DUPLICATE_ERROR);
		}
	}

	private void validateRegistServers(ShardingAge age, List<RealServer> newServers) throws MetaDataException {

		ServerAge serverAge = ages.get(age.getNo());
		if (serverAge != null) {
			throw new MetaDataException(ErrorCodes.META_DATA_ALREADY_EXISTS);
		}
		/*
		 * for(RealServer server : newServers) { if(serverAge.getSize() >
		 * server.getNo()) { throw new
		 * MetaDataException(ErrorCodes.META_DATA_ALREADY_EXISTS); } }
		 */
	}

	public void registServer(Authentication auth, ShardingAge age, List<RealServer> newServers)
			throws MetaDataException {
		assertAuth(auth);
		validateRegistServers(age, newServers);
		try {
			if (CollectionUtils.isNotEmpty(newServers)) {
				ServerAge newAge = new ServerAge(age.getNo(), age.valve(), age.lastValve());
				List<RealServerInfo> ageServers = Lists.newArrayList();
				for (RealServer realServer : newServers) {
					RealServerInfo serverInfo = new RealServerInfo(age.getNo(), realServer.getNo(),
							realServer.getHost(), realServer.getPort(), realServer.getProxyPort());
					serverInfo.setVirtualServerInfo(realServer.getNodes());
					ageServers.add(serverInfo);
				}
				newAge.setServers(ageServers);
				newAge.save(true);
				ages.put(age.getNo(), newAge);
			}
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}

	public void update(Authentication auth, ShardingAge age, List<RealServer> newServers) throws DeadSeaException {
		// TODO Auto-generated method stub

		assertAuth(auth);

		try {
			ServerAge serverAge = ages.get(age.getNo());
			if (serverAge == null) {
				serverAge = new ServerAge(age.getNo());
				ages.put(age.getNo(), serverAge);
			}
			validateUpdateServers(auth, serverAge, age, newServers);
			List<RealServerInfo> realServers = serverAge.getServers();
			for (int i = 0; i < newServers.size(); i++) {
				RealServer metaData = newServers.get(i);
				RealServerInfo rsi = realServers.get(metaData.getNo());
				rsi.setHost(metaData.getHost());
				rsi.setPort(metaData.getPort());
				rsi.setProxyPort(metaData.getProxyPort());
				rsi.save();

			}
			// 刷新其它服务器的副本，逼免出现数据不一致
			serverAge.save();
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}

	private void validateUpdateServers(Authentication auth, ServerAge serverAge, ShardingAge age,
			List<RealServer> newServers) throws MetaDataException {
		try {
			List<RealServerInfo> registedServers = serverAge.getServers();
			for (int i = 0; i < registedServers.size(); i++) {
				RealServer server = newServers.get(i);
				RealServerInfo rsi = registedServers.get(i);
				if (server.getNo() >= serverAge.getServers().size()) {
					throw new MetaDataException(ErrorCodes.META_DATA_INFO_CONFLICT);
				}
			}
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_NOT_EXISTS);
		}
	}

	public ServerAge getAge(Integer ageNo) throws MetaDataException {
		ServerAge age = ages.get(ageNo);
		if (age == null) {
			try {
				age = new ServerAge(ageNo);
			} catch (Exception e) {
				throw new MetaDataException(ErrorCodes.META_DATA_NOT_EXISTS);
			}
		}
		return age;
	}

	public boolean exists(Authentication auth, MetaData data) throws MetaDataException {
		try {
			Account account = getAccountInfo(auth);
			switch (data.getType()) {
			case DB:
				switch (data.getType().getSubType()) {
				case DATABASE:
					return account.findDatabaseInfo(data) != null;
				case TABLE:
					return account.findTableInfo(data) != null;
				case AGE:
					break;
				}
				break;
			case CACHE:
				break;
			}
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_ERROR);
		}
		return false;
	}

	public void removeDb(Authentication auth, MetaData data) throws MetaDataException {
		try {
			Account account = getAccountInfo(auth);
			DatabaseInfo dbInfo = account.findDatabaseInfo(data);
			dbInfo.delete();
			account.refresh();
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}

	}

	public void removeTable(Authentication auth, MetaData data) throws MetaDataException {
		try {
			Account account = getAccountInfo(auth);
			TableInfo table = account.findTableInfo(data);
			table.delete();
			account.refresh();
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}
	
	
	public ServerRegistration getServerRegistration(Authentication auth,ShardingAge age) throws DeadSeaException {
		try {
			assertAuth(auth);
			SimpleRegistration registration = new SimpleRegistration();
			ServerAge serverAge = getAge(age.getNo());
			//int currentSize = serverDatas
			List<RealServerInfo> serverDatas = serverAge.getServers();
			List<RealServer> serverMetaDatas = Lists.newArrayList();
			for(int i = 0;i < serverDatas.size();i++) {
				serverMetaDatas.add(serverDatas.get(i).toMetaData());
			}
			registration.setAllServers(serverMetaDatas);
			ServerScalerBuilder builder = new ServerScalerBuilder();
			registration.setCalculator(builder.build(Lists.newArrayList(accounts.values()), serverAge));
			return registration;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public GlobalInfoDTO getGlobal() {
		return baseInfo.toMetaData().cast();
	}
	
}
