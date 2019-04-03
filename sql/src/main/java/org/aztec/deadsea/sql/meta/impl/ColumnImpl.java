package org.aztec.deadsea.sql.meta.impl;

import org.aztec.deadsea.sql.meta.Location;
import org.aztec.deadsea.sql.meta.Table;

public class ColumnImpl extends BaseMetaData implements org.aztec.deadsea.sql.meta.Column {
	private Table table;

	public ColumnImpl(Table table, String name, String alias, Location location) {
		super(name, alias, location);
		this.table = table;
	}

	public Table table() {
		return table;
	}

}
