package org.aztec.deadsea.common;

import java.util.List;

public interface MetaData {

	public enum MetaType{
		DB,CACHE;
	}
	
	public String getName();
	public Integer getSize();
	public Boolean shard();
	public MetaType getType();
	public List<ShardingAge> getAges();
	public <T> T get(String key);
}
