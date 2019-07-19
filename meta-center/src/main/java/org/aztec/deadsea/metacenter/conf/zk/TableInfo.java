package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.zk.AbstractSubNodeReloader;
import org.aztec.autumn.common.zk.CallableWatcher;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.entity.TableDTO;

import com.google.common.collect.Lists;

public class TableInfo extends ZkConfig {
	
	private Integer no;
	private String name;
	private Integer size;
	@Ignored
	private Integer ageNum;
	private Boolean shard;
	private Long recordSeqNo;
	@Ignored
	private List<ShardingAgeInfo> ages;

	@Ignored
	private List<TimeLimitedCallable> callbacks; 
	
	public TableInfo(String dbPrefix,int no) throws Exception {
		super(dbPrefix + GlobalConst.ZOOKEEPER_PATH_SPLITOR + no,ConfigFormat.JSON);
		initTable();
	}
	
	
	public TableInfo(String path) throws Exception {
		super(path,ConfigFormat.JSON);
		initTable();
	}
	
	public TableInfo(String dbPrefix,Integer no, String name, Integer size, Boolean shard,
			List<ShardingAgeInfo> ages) throws Exception {
		super(dbPrefix + GlobalConst.ZOOKEEPER_PATH_SPLITOR + no, ConfigFormat.JSON);
		this.no = no;
		this.name = name;
		this.size = size;
		this.shard = shard;
		this.ages = ages;
		initTable();
	}

	private void initTable() throws Exception {
		if(!isDeprecated){
			callbacks = Lists.newArrayList();
			AgeReloader loader = new AgeReloader(this);
			callbacks.add(loader);
			loader.load();
			appendWatcher(new CallableWatcher(callbacks, null));
		}
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void destroy() {
		super.destroy();
		for(ShardingAgeInfo age : ages) {
			age.destroy();
		}
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
	

	public Integer getAgeNum() {
		return ageNum;
	}


	public void setAgeNum(Integer ageNum) {
		this.ageNum = ageNum;
	}
	
	public MetaData toMetaData(MetaData db) {
		TableDTO table = new TableDTO(no,name,size,shard,db.cast());
		for(ShardingAgeInfo sa : ages) {
			table.getChilds().add(sa.toMetaData());
		}
		return table;
	}


	private class AgeReloader extends AbstractSubNodeReloader {

		public AgeReloader(ZkConfig parent) {
			super(parent);
			// TODO Auto-generated constructor stub
		}
	
		public Long getTime() {
			BaseInfo baseInfo = BaseInfo.getInstance();
			return baseInfo.getLoadTableTimeout();
		}

		public TimeUnit getUnit() {
			return TimeUnit.MILLISECONDS;
		}

		@Override
		protected ZkConfig loadChild(int index) throws Exception {
			return new ShardingAgeInfo(znode, index);
		}
		
	}

	public Long getRecordSeqNo() {
		return recordSeqNo;
	}

	public void setRecordSeqNo(Long recordSeqNo) {
		this.recordSeqNo = recordSeqNo;
	}

}
