package org.aztec.deadsea.sql.meta.impl;

import org.aztec.deadsea.sql.meta.DataType;
import org.aztec.deadsea.sql.meta.Location;
import org.aztec.deadsea.sql.meta.Table;

public class ColumnImpl extends BaseMetaData implements org.aztec.deadsea.sql.meta.Column {
	private Table table;
	private DataType type;

	public ColumnImpl(Table table, String name, String alias, Location location) {
		super(name, alias, location);
		this.table = table;
	}

	public Table table() {
		return table;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public DataType type() {
		// TODO Auto-generated method stub
		return type;
	}

}
