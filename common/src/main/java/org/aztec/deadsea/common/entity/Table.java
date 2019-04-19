package org.aztec.deadsea.common.entity;

public class Table extends BaseMetaData{

	public Table(String name,Integer size,boolean shard) {
		super(name, size, shard);
		this.type = MetaType.DB;
	}

}
