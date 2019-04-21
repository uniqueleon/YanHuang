package org.aztec.deadsea.common;

import java.util.List;

public interface MetaData {

	public enum MetaType{
		DB,CACHE;
		
		private MetaSubType subType;

		public MetaSubType getSubType() {
			return subType;
		}

		public void setSubType(MetaSubType subType) {
			this.subType = subType;
		}
		
	}
	
	public enum MetaSubType{
		DATABASE,TABLE;
	}
	
	public int getSeqNo();
	public String getName();
	public Integer getSize();
	public Boolean shard();
	public MetaType getType();
	public List<ShardingAge> getAges();
	public <T> T get(String key);
	public <T> T cast();
	public MetaData getParent();
	public List<MetaData> getChilds();
}
