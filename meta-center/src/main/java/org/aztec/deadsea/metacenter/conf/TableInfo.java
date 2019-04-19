package org.aztec.deadsea.metacenter.conf;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.metacenter.MetaCenterConst;

public class TableInfo extends ZkConfig {
	
	private Integer no;
	private String name;
	private Integer size;
	private Boolean shard;
	@Ignored
	private List<ShardingAgeInfo> ages;

	public TableInfo(int no) throws IOException, KeeperException, InterruptedException {
		super(MetaCenterConst.ZkConfigPaths.TABLES_SHARDING_INFO + "." + no, ConfigFormat.JSON);
		this.no = no;
	}
	
	
	
	public TableInfo(Integer no, String name, Integer size, Boolean shard,
			List<ShardingAgeInfo> ages) throws IOException, KeeperException, InterruptedException {
		super(MetaCenterConst.ZkConfigPaths.TABLES_SHARDING_INFO + "." + no, ConfigFormat.JSON);
		this.no = no;
		this.name = name;
		this.size = size;
		this.shard = shard;
		this.ages = ages;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void destroy() {
		super.destroy();
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Boolean getShard() {
		return shard;
	}

	public void setShard(Boolean shard) {
		this.shard = shard;
	}

	public List<ShardingAgeInfo> getAges() {
		return ages;
	}

	public void setAges(List<ShardingAgeInfo> ages) {
		this.ages = ages;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

}
