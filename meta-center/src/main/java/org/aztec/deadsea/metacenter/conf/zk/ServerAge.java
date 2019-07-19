package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.AbstractSubNodeReloader;
import org.aztec.autumn.common.zk.CallableWatcher;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.MetaDataConstant;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.common.VirtualServer;
import org.aztec.deadsea.common.entity.ServerAgeDTO;
import org.aztec.deadsea.metacenter.MetaCenterConst;

import com.google.common.collect.Lists;

public class ServerAge  extends ZkConfig{
	
	private Integer age;
	@Ignored
	private Integer serverNum;
	private Long valve;
	private Long lastValve;
	@Ignored
	private List<TimeLimitedCallable>  callBacks;
	@Ignored
	private List<RealServerInfo> servers;
	
	public ServerAge(int age,Long valve,Long lastValve) throws IOException, KeeperException, InterruptedException {
		super(String.format(MetaCenterConst.ZkConfigPaths.REAL_SERVER_AGE_INFO,new Object[] {age}), ConfigFormat.JSON);
		this.age = age;
		this.valve = valve;
		this.lastValve = lastValve;
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
		if(isDeprecated) {
			return;
		}
		callBacks = Lists.newArrayList();
		callBacks.add(new ServerReloader(this));
		appendWatcher(new CallableWatcher(callBacks, null));
	}
	

	private class ServerReloader extends AbstractSubNodeReloader {
		
		public ServerReloader(ZkConfig parent) {
			super(parent);
		}

		public ZkConfig loadChild(int index) throws Exception {
			return new RealServerInfo(age,index);
		}

		@Override
		public Long getTime() {
			BaseInfo baseInfo = BaseInfo.getInstance();
			return baseInfo.getLoadServerTimeout();
		}
		
	}
	

	public List<RealServerInfo> getServers() {
		return servers;
	}

	
	public ShardingAge toMetaData() {
		ServerAgeDTO ageDto = new ServerAgeDTO(age, MetaDataConstant.DEFAULT_SERVER_AGE_NAME_PREFIX + age, serverNum,
				valve, lastValve);
		return ageDto;
	}

	public void save(boolean cascade) throws Exception {
		super.save();
		try {
			if(cascade) {
				for(RealServerInfo vs : servers) {
					vs.save(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			super.delete();
		}
	}

	public void setServers(List<RealServerInfo> servers) {
		this.servers = servers;
	}
	
	
	
}
