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
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataConstant;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.common.entity.ServerAgeDTO;
import org.aztec.deadsea.metacenter.MetaCenterConst;
import org.aztec.deadsea.metacenter.MetaCenterLogger;

import com.google.common.collect.Lists;

public class ServerAge  extends ZkConfig{
	
	private Integer age;
	private Integer size;
	private Long valve;
	private Long lastValve;
	@Ignored
	private List<TimeLimitedCallable>  callBacks;
	@Ignored
	private List<RealServerInfo> servers;
	
	public ServerAge(int age,int size,Long valve,Long lastValve) throws IOException, KeeperException, InterruptedException {
		super(String.format(MetaCenterConst.ZkConfigPaths.REAL_SERVER_AGE_INFO,new Object[] {age}), ConfigFormat.JSON);
		this.age = age;
		this.valve = valve;
		this.lastValve = lastValve;
		this.size = size;
		initInfo();
	}
	
	public ServerAge(int age) throws IOException, KeeperException, InterruptedException {
		super(String.format(MetaCenterConst.ZkConfigPaths.REAL_SERVER_AGE_INFO,new Object[] {age}), ConfigFormat.JSON);
		this.age = age;
		initInfo();
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Long getValve() {
		return valve;
	}

	public void setValve(Long valve) {
		this.valve = valve;
	}

	public Long getLastValve() {
		return lastValve;
	}

	public void setLastValve(Long lastValve) {
		this.lastValve = lastValve;
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
			for(int i = 0;i < size ;i++) {
				servers.add(new RealServerInfo(age,i));
			}
		}

		public Long getTime() {
			BaseInfo baseInfo = BaseInfo.getInstance();
			return baseInfo.getLoadServerTimeout();
		}

		public TimeUnit getUnit() {
			return TimeUnit.MILLISECONDS;
		}

		public void interupt() {
			// TODO Auto-generated method stub
			
		}
		
	}
	

	public List<RealServerInfo> getServers() {
		return servers;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	public MetaData toMetaData() {
		ServerAgeDTO ageDto = new ServerAgeDTO(MetaDataConstant.DEFAULT_SERVER_AGE_NAME_PREFIX + age, size);
		return ageDto;
	}
}
