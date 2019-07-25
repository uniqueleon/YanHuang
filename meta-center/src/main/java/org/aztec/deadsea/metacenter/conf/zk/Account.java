package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.AbstractSubNodeReloader;
import org.aztec.autumn.common.zk.CallableWatcher;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.metacenter.MetaCenterConst;

import com.google.common.collect.Lists;

public class Account extends ZkConfig{
	
	private String username;
	private String password;
	private String uuid;
	@Ignored
	private Integer dbNum;
	
	@Ignored
	private List<TimeLimitedCallable>  callBacks;
	@Ignored
	private List<DatabaseInfo> databases;
	
	DatabaseReloader loader;

	public Account(String uuid) throws Exception {
		// TODO Auto-generated constructor stub
		super(String.format(MetaCenterConst.ZkConfigPaths.BASE_AUTHENTICATIONS_INFO, new Object[] {uuid}), ConfigFormat.JSON);
		initAccount();
	}
	
	private void initAccount() throws Exception  {
		if(!isDeprecated) {
			callBacks = Lists.newArrayList();
			loader = new DatabaseReloader(this);
			loader.load();
			callBacks.add(loader);
			dbNum = getSubNodes().size();
			appendWatcher(new CallableWatcher(callBacks, null));
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getDbNum() {
		return dbNum;
	}

	public void setDbNum(Integer dbNum) {
		this.dbNum = dbNum;
	}

	public List<DatabaseInfo> getDatabases() {
		return databases;
	}

	public void setDatabases(List<DatabaseInfo> databases) {
		this.databases = databases;
	}
	

	private class DatabaseReloader extends AbstractSubNodeReloader {

		public DatabaseReloader(ZkConfig parent) {
			super(parent);
			// TODO Auto-generated constructor stub
		}

		public Long getTime() {
			BaseInfo baseInfo = BaseInfo.getInstance();
			return baseInfo.getLoadTableTimeout();
		}

		@Override
		protected ZkConfig loadChild(int index) throws Exception {
			// TODO Auto-generated method stub
			return new DatabaseInfo(znode,index);
		}

		@Override
		protected void setChildrens(List children) throws Exception {
			// TODO Auto-generated method stub
			databases = children;
		}
		
	}
	
	public void refresh() throws Exception {
		loader.load();
		dbNum = getSubNodes().size();
		save();
	}
	
	public DatabaseInfo findDatabaseInfo(final MetaData param) {
		if(databases.size() <= param.getSeqNo()) {
			return null;
		}
		return databases.get(param.getSeqNo());
	}
	
	public TableInfo findTableInfo(MetaData param) {
		DatabaseInfo dbInfo = findDatabaseInfo(param.getParent());
		if(dbInfo != null) {
			if(dbInfo.getTables().size() > param.getSeqNo()) {
				return dbInfo.getTables().get(param.getSeqNo());
			}
		}
		return null;
	}
}
