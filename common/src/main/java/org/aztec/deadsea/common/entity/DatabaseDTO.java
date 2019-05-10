package org.aztec.deadsea.common.entity;

import java.util.List;

import org.aztec.deadsea.common.MetaData;

import com.google.common.collect.Lists;

public class DatabaseDTO extends BaseMetaData implements MetaData {
	
	private int no;
	
	private Integer tableNum;
	
	
	private List<TableDTO> tables;

	public DatabaseDTO(int no,String name,int size,int tableNum,boolean shard,List<TableDTO> tables) {
		super(no,name, size, shard);
		tables = Lists.newArrayList();
		this.type = MetaType.DB;
		this.type.setSubType(MetaSubType.DATABASE);
		for(TableDTO table : tables){
			childs.add(table);
		}
		this.tableNum = tableNum;
		
	}

	public List<TableDTO> getTables() {
		return tables;
	}

	public void setTables(List<TableDTO> tables) {
		this.tables = tables;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
	public Integer getTableNum() {
		return tableNum;
	}

	public void setTableNum(Integer tableNum) {
		this.tableNum = tableNum;
	}
	
	
	
}
