package org.aztec.deadsea.common.entity;

import java.util.List;

import org.aztec.deadsea.common.MetaData;

import com.google.common.collect.Lists;

public class Database extends BaseMetaData implements MetaData {
	
	private int no;
	
	private List<Table> tables;

	public Database(int no,String name,int size,int tableNum,boolean shard,List<Table> tables) {
		super(no,name, size, shard);
		tables = Lists.newArrayList();
		this.type = MetaType.DB;
		this.type.setSubType(MetaSubType.DATABASE);
		for(Table table : tables){
			childs.add(table);
		}
		
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
}
