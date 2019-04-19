package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.CallableWatcher;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.metacenter.MetaCenterConst;
import org.aztec.deadsea.metacenter.MetaCenterLogger;
import org.aztec.deadsea.metacenter.MetaDataException;

import com.google.common.collect.Lists;

public class BaseInfo extends ZkConfig {
	
	// 逻辑表数
	private Integer tableNum;
	// 表分片数
	private Integer tableSize;
	// 最大 age
	private Integer maxAge;
	private Long loadServerTimeout;
	private Long loadTableTimeout;
	private Long loadAgesTimeout;
	private Long realNum;
	private Long virtualNum;
	private String type;
	private static BaseInfo instance;
	@Ignored
	private List<RealServerInfo> servers;
	@Ignored
	private List<Account> accounts;
	@Ignored
	private List<TimeLimitedCallable>  callBacks;
	
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
		initInfo();
	}
	
	private void initInfo() {
		callBacks = Lists.newArrayList();
		callBacks.add(new ServerReloader());
		appendWatcher(new CallableWatcher(callBacks, null));
		final ExecutorService service = Executors.newFixedThreadPool(1);
		for(TimeLimitedCallable task : callBacks) {
			try {
				Future future = service.submit(task);
				future.get(task.getTime(), task.getUnit());
			} catch (Exception e) {
				MetaCenterLogger.error(e.getMessage());
			}
		}
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
	
	
	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	
	private class ServerReloader implements TimeLimitedCallable{

		public Object call() throws Exception {
			if(!CollectionUtils.isEmpty(servers)) {
				for(RealServerInfo server : servers) {
					server.destroy();
				}
				servers.clear();
			}
			loadServer();
			return null;
		}
		
		public  void loadServer() throws IOException, KeeperException, InterruptedException {
			servers = Lists.newArrayList();
			for(int i = 0;i < realNum ;i++) {
				servers.add(new RealServerInfo(i));
			}
		}

		public Long getTime() {
			return loadServerTimeout;
		}

		public TimeUnit getUnit() {
			return TimeUnit.MILLISECONDS;
		}

		public void interupt() {
			// TODO Auto-generated method stub
			
		}
		
	}

	public Integer getTableNum() {
		return tableNum;
	}

	public void setTableNum(Integer tableNum) {
		this.tableNum = tableNum;
	}

	public Long getLoadServerTimeout() {
		return loadServerTimeout;
	}

	public void setLoadServerTimeout(Long loadServerTimeout) {
		this.loadServerTimeout = loadServerTimeout;
	}

	public Long getLoadTableTimeout() {
		return loadTableTimeout;
	}

	public void setLoadTableTimeout(Long loadTableTimeout) {
		this.loadTableTimeout = loadTableTimeout;
	}

	public Long getLoadAgesTimeout() {
		return loadAgesTimeout;
	}

	public void setLoadAgesTimeout(Long loadAgesTimeout) {
		this.loadAgesTimeout = loadAgesTimeout;
	}

	public List<RealServerInfo> getServers() {
		return servers;
	}



	
	public void registServer(RealServerInfo serverInfo) throws MetaDataException {
		try {
			if(servers.size() > serverInfo.getNo()) {
				RealServerInfo thisInfo = servers.get(serverInfo.getNo());
				thisInfo.destroy();
				servers.set(serverInfo.getNo(),serverInfo);
			}
			else {
				servers.add(serverInfo);
			}
			serverInfo.save();
		} catch (Exception e) {
			MetaCenterLogger.error(e);
			throw new MetaDataException(2);
		}
	}
	
}
