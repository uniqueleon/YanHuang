package org.aztec.deadsea.common.entity;

import org.aztec.deadsea.common.MetaData;

public class GlobalInfoDTO extends BaseMetaData implements MetaData{
	
	// 逻辑表数
	private Integer tableNum;
	// 表分片数
	private Integer tableSize;
	// 最大 age
	private Integer maxAge;
	private String type;
	
	private String accessString;

	public GlobalInfoDTO(Integer tableNum,Integer maxAge,String type,Integer tableSize,String accessString) {
		// TODO Auto-generated constructor stub
		super("GLOBAL_INFORMATION", 1, true);
		super.type = type.equals("DB") ? MetaType.DB : MetaType.CACHE;
	}

	public Integer getTableNum() {
		return tableNum;
	}

	public void setTableNum(Integer tableNum) {
		this.tableNum = tableNum;
	}

	public Integer getTableSize() {
		return tableSize;
	}

	public void setTableSize(Integer tableSize) {
		this.tableSize = tableSize;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccessString() {
		return accessString;
	}

	public void setAccessString(String accessString) {
		this.accessString = accessString;
	}

	
}
