package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.metacenter.MetaCenterConst;

import com.google.common.collect.Lists;

public class Account extends ZkConfig{
	
	private String username;
	private String password;
	private String uuid;
	private Integer dbNum;
	
	@Ignored
	private List<TimeLimitedCallable>  callBacks;
	@Ignored
	private List<DatabaseInfo> databases;

	public Account(String uuid) throws IOException, KeeperException, InterruptedException {
		// TODO Auto-generated constructor stub
		super(String.format(MetaCenterConst.ZkConfigPaths.BASE_AUTHENTICATIONS_INFO, new Object[] {uuid}), ConfigFormat.JSON);
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

	private class DatabaseReloader implements TimeLimitedCallable {

		public Object call() throws Exception {
			if(!CollectionUtils.isEmpty(databases)) {
				for(DatabaseInfo database : databases) {
					database.destroy();
				}
				databases.clear();
			}
			loadTables();
			return null;
		}
		
		public void loadTables() throws IOException, KeeperException, InterruptedException {
			
			databases = Lists.newArrayList();
			if(dbNum == null) {
				dbNum = 0;
			}
			for(int i = 0;i < dbNum ;i++) {
				databases.add(new DatabaseInfo(znode,i));
			}
		}

		public Long getTime() {
			BaseInfo baseInfo = BaseInfo.getInstance();
			return baseInfo.getLoadTableTimeout();
		}

		public TimeUnit getUnit() {
			return TimeUnit.MILLISECONDS;
		}

		public void interupt() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}