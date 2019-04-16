package org.aztec.deadsea.metacenter.conf;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.metacenter.MetaCenterConst;
import org.aztec.deadsea.metacenter.MetaCenterLogger;

import com.google.common.collect.Lists;

public class BaseInfo extends ZkConfig {
	
	private String dbPrefix;
	private Integer dbSize;
	private Integer tableNum;
	private Integer tableSize;
	private Long loadServerTimeout;
	private Long loadTableTimeout;
	private Long loadAgesTimeout;
	private Long realNum;
	private Long virtualNum;
	private String type;
	@Ignored
	private List<RealServerInfo> realServers;
	@Ignored
	private List<TableInfo> tables;
	@Ignored
	private List<ShardingAgeInfo> ages;
	
	private static BaseInfo instance;
	
	static {
		try {
			instance = new BaseInfo();
		} catch (Exception e) {
			MetaCenterLogger.error(e);
		}
	}

	private BaseInfo()
			throws IOException, KeeperException, InterruptedException {
		super(MetaCenterConst.ZkConfigPaths.BASE_INFO, ConfigFormat.JSON);
	}

	public String getDbPrefix() {
		return dbPrefix;
	}

	public void setDbPrefix(String dbPrefix) {
		this.dbPrefix = dbPrefix;
	}

	public Integer getDbSize() {
		return dbSize;
	}

	public void setDbSize(Integer dbSize) {
		this.dbSize = dbSize;
	}

	public Integer getTableSize() {
		return tableSize;
	}

	public void setTableSize(Integer tableSize) {
		this.tableSize = tableSize;
	}

	public static BaseInfo getInstance() {
		return instance;
	}



	public Long getRealNum() {
		return realNum;
	}

	public void setRealNum(Long realNum) {
		this.realNum = realNum;
	}

	public Long getVirtualNum() {
		return virtualNum;
	}

	public void setVirtualNum(Long virtualNum) {
		this.virtualNum = virtualNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	private class TableReloader implements TimeLimitedCallable {

		public Object call() throws Exception {
			if(tables != null && tables.size() > 0) {
				for(TableInfo table : tables) {
					table.destroy();
				}
			}
			tables.clear();
			return null;
		}
		
		public void loadTables() {
			tables = Lists.newArrayList();
			for(int i = 0;i < tableNum ;i++) {
				
			}
		}

		public Long getTime() {
			return loadTableTimeout;
		}

		public TimeUnit getUnit() {
			return TimeUnit.MILLISECONDS;
		}
		
	}
	
	private class ShardingAgeReloader implements TimeLimitedCallable{

		public Object call() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		public Long getTime() {
			// TODO Auto-generated method stub
			return null;
		}

		public TimeUnit getUnit() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
