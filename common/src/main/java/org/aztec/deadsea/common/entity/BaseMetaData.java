package org.aztec.deadsea.common.entity;

import java.util.List;
import java.util.Map;

import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.ShardingAge;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;

public class BaseMetaData implements MetaData {
	
	protected int no;
	protected String name;
	protected Integer size;
	protected Boolean shard;
	protected MetaType type;
	protected Map<String,Object> attachments;
	protected List<ShardingAge> ages;
	protected MetaData parent;
	protected List<MetaData> childs;

	public BaseMetaData(String name, Integer size, Boolean shard) {
		super();
		this.name = name;
		this.size = size;
		this.shard = shard;
		attachments = Maps.newHashMap();
		childs = Lists.newArrayList();
	}
	
	public BaseMetaData(int no,String name, Integer size, Boolean shard) {
		super();
		this.name = name;
		this.size = size;
		this.shard = shard;
		attachments = Maps.newHashMap();
		this.no = no;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public Integer getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	public Boolean shard() {
		// TODO Auto-generated method stub
		return shard;
	}

	public MetaType getType() {
		return type;
	}

	public <T> T get(String key) {
		return (T) attachments.get(key);
	}

	public List<ShardingAge> getAges() {
		return ages;
	}

	@Override
	public <T> T cast() {
		return (T) this;
	}

	@Override
	public MetaData getParent() {
		return parent;
	}

	@Override
	public List<MetaData> getChilds() {
		return childs;
	}

	@Override
	public int getSeqNo() {
		return no;
	}

}
