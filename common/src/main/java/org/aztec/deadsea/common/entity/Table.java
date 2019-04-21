package org.aztec.deadsea.common.entity;

public class Table extends BaseMetaData{
	
	private Database database;
	private int ageNum;

	public Table(int no,String name,Integer size,boolean shard,Database base) {
		super(no,name, size, shard);
		this.type = MetaType.DB;
		this.type.setSubType(MetaSubType.TABLE);
		this.parent = base;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public int getAgeNum() {
		return ageNum;
	}

	public void setAgeNum(int ageNum) {
		this.ageNum = ageNum;
	}

	
}
