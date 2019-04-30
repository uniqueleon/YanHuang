package org.aztec.deadsea.metacenter.impl;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.utils.security.CodeCipher;
import org.aztec.autumn.common.zk.ZkUtils;
import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.entity.Database;
import org.aztec.deadsea.common.entity.ShardAge;
import org.aztec.deadsea.common.entity.SimpleAuthentication;
import org.aztec.deadsea.common.entity.Table;
import org.aztec.deadsea.metacenter.MetaCenterConst;
import org.aztec.deadsea.metacenter.MetaCenterLogger;
import org.aztec.deadsea.metacenter.MetaDataException;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;
import org.aztec.deadsea.metacenter.conf.zk.Account;
import org.aztec.deadsea.metacenter.conf.zk.DatabaseInfo;
import org.aztec.deadsea.metacenter.conf.zk.ShardingAgeInfo;
import org.aztec.deadsea.metacenter.conf.zk.TableInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ZkRegistHelper {

	private CodeCipher cipher = new CodeCipher();

	public ZkRegistHelper() {
		// TODO Auto-generated constructor stub
	}

	public void updateDB(Map<String,Account> accounts,Authentication auth, MetaData data) throws MetaDataException {
		try {
			Database db = data.cast();
			DatabaseInfo dbInfo = new DatabaseInfo(auth.getUUID());
			if(data.getName() != null) {
				dbInfo.setName(data.getName());
			}
			if(data.getSize() != null) {
				dbInfo.setSize(data.getSize());
			}
			if(data.shard() != null) {
				dbInfo.setShard(data.shard());
			}
			if(db.getTableNum() != null) {
				dbInfo.setTableNum(db.getTableNum());
			}
			dbInfo.save();
			//only to triger server synchorization!
			Account account = accounts.get(auth.getUUID());
			account.save();
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}
	
	public void updateTable(Map<String,Account> accounts,Authentication auth, MetaData data) throws MetaDataException {

		String dbPrefix = String.format(MetaCenterConst.ZkConfigPaths.DATABASE_INFO,
				new Object[] { auth.getUUID(), data.getParent().getSeqNo() });
		try {
			Table tbData = data.cast();
			TableInfo table = new TableInfo(dbPrefix, data.getSeqNo());
			if(data.getName() != null) {
				table.setName(data.getName());
			}
			if(table.getSize() != null) {
				table.setSize(data.getSize());
			}
			if(data.shard() != null) {
				table.setShard(data.shard());
			}
			table.setAgeNum(tbData.getAgeNum());
			table.setRecordSeqNo(tbData.getRecordSeqNo());
			table.save();
			DatabaseInfo db = accounts.get(auth.getUUID()).getDatabases().get(data.getParent().getSeqNo());
			db.save();
		} catch (Exception e) {

			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}
	


	public void registAge(Map<String,Account> accounts,Authentication auth, MetaData data) throws MetaDataException {

		String tablePrefix = String.format(MetaCenterConst.ZkConfigPaths.SHARDING_AGE_INFO,
				new Object[] { auth.getUUID(), data.getParent().getParent().getSeqNo(), data.getParent().getSeqNo() });
		
		try {
			TableInfo table = accounts.get(auth.getUUID()).getDatabases().get(data.getParent().getParent().getSeqNo())
					.getTables().get(data.getParent().getSeqNo());
			ShardingAgeInfo ageInfo = new ShardingAgeInfo(tablePrefix, data.getSeqNo());
			ShardAge sAge = data.cast();
			ageInfo.setModulus(sAge.getModulus());
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
		switch(data.getType().getSubType()) {
		case DATABASE:
			Database db = data.cast();
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
			String tablePrefix = String.format(MetaCenterConst.ZkConfigPaths.SHARDING_AGE_INFO,
					new Object[] { auth.getUUID(), data.getParent().getParent().getSeqNo(), data.getParent().getSeqNo() });
			if (!ZkUtils.isNodeExists(tablePrefix)) {
				throw new MetaDataException(ErrorCodes.META_DATA_NOT_EXISTS);
			}
			break;
		}
	}

	public void registDB(Map<String,Account> accounts,Authentication auth, MetaData data) throws MetaDataException {

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
		} catch (Exception e) {
			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}

	public void registTable(Map<String,Account> accounts,Authentication auth, MetaData data) throws MetaDataException {

		String dbPrefix = String.format(MetaCenterConst.ZkConfigPaths.DATABASE_INFO,
				new Object[] { auth.getUUID(), data.getParent().getSeqNo() });
		try {
			TableInfo table = new TableInfo(dbPrefix, data.getSeqNo());
			table.setName(data.getName());
			table.setSize(data.getSize());
			table.setShard(data.shard());
			table.setAges(Lists.newArrayList());
			table.setNo(data.getSeqNo());
			table.setAgeNum(0);
			table.setRecordSeqNo(0l);
			table.save();
			DatabaseInfo db = accounts.get(auth.getUUID()).getDatabases().get(data.getParent().getSeqNo());
			db.setTableNum(db.getTableNum() + 1);
			db.save();
		} catch (Exception e) {

			throw new MetaDataException(ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}
	
	

	public void assertAuth(Authentication auth) throws MetaDataException {
		if (!auth.isAuthenticated()) {
			throw new MetaDataException(ErrorCodes.NOT_AUTHORIZED);
		}
	}
	
	public Map<String, List<MetaData>> getRegistedMetaDatas(Map<String,Account> accounts,Authentication auth) throws DeadSeaException {

		assertAuth(auth);
		Map<String, List<MetaData>> dataMap = Maps.newHashMap();
		String base64 = cipher.encodeBase64(auth.getName());
		Account account = accounts.get(base64);
		List<DatabaseInfo> databases = account.getDatabases();
		List<MetaData> dbs = Lists.newArrayList();
		for (DatabaseInfo dbInfo : databases) {
			dataMap.put(MetaDataRegister.MetaDataMapKeys.DATA_BASE_KEY, dbs);
		}
		return dataMap;
	}
	
	public Authentication auth(Map<String,Account> accounts,String username, String password) throws DeadSeaException {
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
				account.setUsername(username);
				account.setPassword(cipher.getMD5Substract(password, GlobalConst.DEFAULT_CHARSET));
				account.save();
				return account;
			} catch (Exception e) {
				MetaCenterLogger.error(e);
				return null;
			}

		} else {
			throw new MetaDataException(ErrorCodes.AUTHENTICATION_DUPLICATE_ERROR);
		}
	}
}
