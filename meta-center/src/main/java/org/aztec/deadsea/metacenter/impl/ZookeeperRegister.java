package org.aztec.deadsea.metacenter.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.utils.security.CodeCipher;
import org.aztec.autumn.common.zk.ZkUtils;
import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaData.MetaType;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.ServerRegistration;
import org.aztec.deadsea.common.entity.Database;
import org.aztec.deadsea.common.entity.ShardAge;
import org.aztec.deadsea.common.entity.SimpleAuthentication;
import org.aztec.deadsea.metacenter.MetaCenterConst;
import org.aztec.deadsea.metacenter.MetaCenterLogger;
import org.aztec.deadsea.metacenter.MetaDataException;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;
import org.aztec.deadsea.metacenter.conf.zk.Account;
import org.aztec.deadsea.metacenter.conf.zk.BaseInfo;
import org.aztec.deadsea.metacenter.conf.zk.DatabaseInfo;
import org.aztec.deadsea.metacenter.conf.zk.ShardingAgeInfo;
import org.aztec.deadsea.metacenter.conf.zk.TableInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ZookeeperRegister implements ServerRegister, MetaDataRegister {

	private static final BaseInfo baseInfo = BaseInfo.getInstance();
	private CodeCipher cipher = new CodeCipher();
	
	private static Map<String,Account> accounts = Maps.newConcurrentMap();

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
				/*TableInfo table = new TableInfo(curTableNum, data.getName(), data.getSize(), data.shard(),
						new ArrayList<ShardingAgeInfo>());*/
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
		
		String base64 = cipher.encodeBase64(username);
		String node = String.format(MetaCenterConst.ZkConfigPaths.BASE_AUTHENTICATIONS_INFO,
				new Object[] {base64});
		if(!ZkUtils.isNodeExists(node)) {
			try {
				Account account = new Account(base64);
				account.setUsername(username);
				account.setPassword(cipher.getMD5Substract(password, GlobalConst.DEFAULT_CHARSET));
				account.save();
				return new SimpleAuthentication(username, base64, password, true);
			} catch (Exception e) {
				MetaCenterLogger.error(e);
				return new SimpleAuthentication(username, base64, password, false);
			}
			
		}
		else {
			throw new MetaDataException(ErrorCodes.AUTHENTICATION_DUPLICATE_ERROR);
		}
	}

	@Override
	public Authentication auth(String username, String password) throws DeadSeaException {
		String base64 = cipher.encodeBase64(username);
		String node = String.format(MetaCenterConst.ZkConfigPaths.BASE_AUTHENTICATIONS_INFO,
				new Object[] {base64});
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

	@Override
	public void regist(Authentication auth, MetaData data) throws DeadSeaException {
		// TODO Auto-generated method stub
		if(auth.isAuthenticated()) {
			switch(data.getType()) {
			case DB:
				switch(data.getType().getSubType()) {
				case DATABASE:

					registDB(auth, data);
					break;
				case TABLE:
					registTable(auth,data);
					break;
				case AGE:
					break;
				}
			}
		}
	}
	
	public void registAge(Authentication auth ,MetaData data) throws MetaDataException {

		assertAuth(auth);
		String tablePrefix = String.format(MetaCenterConst.ZkConfigPaths.SHARDING_AGE_INFO,new Object[] {auth.getUUID(),data.getParent().getParent().getSeqNo(),data.getParent().getSeqNo()});
		if(!ZkUtils.isNodeExists(tablePrefix)) {
			throw new MetaDataException(ErrorCodes.META_DATA_NOT_EXISTS);
		}
		try {
			TableInfo table = accounts.get(auth.getUUID()).getDatabases().get(data.getParent().getParent().getSeqNo()).getTables().get(data.getParent().getSeqNo());
			ShardingAgeInfo ageInfo = new ShardingAgeInfo(tablePrefix, data.getSeqNo());
			ShardAge sAge = data.cast();
			ageInfo.setModulus(sAge.getModulus());
			ageInfo.setValve(sAge.getValve());
			ageInfo.save();
			table.setAgeNum(table.getAgeNum() + 1);
			table.save();
		}  catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}
	
	public void registDB(Authentication auth ,MetaData data) throws MetaDataException {

		assertAuth(auth);
		Database db = data.cast();
		String path = String.format(MetaCenterConst.ZkConfigPaths.DATABASE_INFO,new Object[] {auth.getUUID(),db.getSeqNo()});
		if(ZkUtils.isNodeExists(path)) {
			throw new MetaDataException(ErrorCodes.META_DATA_ALREADY_EXISTS);
		}
		try {
			Account account = accounts.get(auth.getUUID());
			DatabaseInfo dbInfo = new DatabaseInfo(auth.getUUID());
			dbInfo.setName(data.getName());
			dbInfo.setSize(data.getSize());
			dbInfo.setShard(data.shard());
			dbInfo.setTableNum(0);
			dbInfo.setNo(data.getSeqNo());
			dbInfo.save();
			account.setDbNum(account.getDbNum() + 1);
			account.save();
		}  catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}
	
	public void registTable(Authentication auth ,MetaData data) throws MetaDataException {

		assertAuth(auth);
		String dbPrefix = String.format(MetaCenterConst.ZkConfigPaths.DATABASE_INFO,new Object[] {auth.getUUID(),data.getParent().getSeqNo()});
		if(!ZkUtils.isNodeExists(dbPrefix)) {
			throw new MetaDataException(ErrorCodes.META_DATA_NOT_EXISTS);
		}
		MetaData parent = data.getParent();
		String tablePath = String.format(MetaCenterConst.ZkConfigPaths.TABLES_SHARDING_INFO,new Object[] {auth.getUUID(),parent.getSeqNo(),data.getSeqNo()});
		if(ZkUtils.isNodeExists(tablePath)) {
			throw new MetaDataException(ErrorCodes.META_DATA_ALREADY_EXISTS);
		}
		try {
			TableInfo table = new TableInfo(dbPrefix,data.getSeqNo());
			table.setName(data.getName());
			table.setSize(data.getSize());
			table.setShard(data.shard());
			table.setAges(Lists.newArrayList());
			table.setNo(data.getSeqNo());
			table.setAgeNum(0);
			table.save();
			DatabaseInfo db = accounts.get(auth.getUUID()).getDatabases().get(data.getParent().getSeqNo());
			db.setTableNum(db.getTableNum() + 1);
			db.save();
		} catch (Exception e) {

			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}
	
	private void assertAuth(Authentication auth)  throws MetaDataException {
		if(!auth.isAuthenticated()) {
			throw  new MetaDataException(ErrorCodes.NOT_AUTHORIZED);
		}
	}

	@Override
	public Map<String, List<MetaData>> getRegistedMetaDatas(Authentication auth) throws DeadSeaException {
		
		assertAuth(auth);
		Map<String,List<MetaData>> dataMap = Maps.newHashMap();
		String base64 = cipher.encodeBase64(auth.getName());
		Account account = accounts.get(base64);
		List<DatabaseInfo> databases = account.getDatabases();
		List<MetaData> dbs = Lists.newArrayList();
		for(DatabaseInfo dbInfo : databases) {
			dataMap.put(MetaDataRegister.MetaDataMapKeys.DATA_BASE_KEY, dbs);
		}
		return dataMap;
	}

	@Override
	public void update(Authentication auth, MetaData data) throws DeadSeaException {
		// TODO Auto-generated method stub
		
	}

}
