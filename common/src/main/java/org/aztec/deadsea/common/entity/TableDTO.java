package org.aztec.deadsea.common.entity;

public class TableDTO extends BaseMetaData{
	
	private DatabaseDTO database;
	private int ageNum;
	private Long recordSeqNo;

	public TableDTO(int no,String name,Integer size,boolean shard,Long recordSeqNo,DatabaseDTO base) {
		super(no,name, size, shard);
		this.type = MetaType.DB;
		this.type.setSubType(MetaSubType.TABLE);
		this.parent = base;
		this.recordSeqNo = recordSeqNo;
	}

	public DatabaseDTO getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseDTO database) {
		this.database = database;
	}

	public int getAgeNum() {
		return ageNum;
	}

	public void setAgeNum(int ageNum) {
		this.ageNum = ageNum;
	}

	public Long getRecordSeqNo() {
		return recordSeqNo;
	}

	public void setRecordSeqNo(Long recordSeqNo) {
		this.recordSeqNo = recordSeqNo;
	}

	
}
