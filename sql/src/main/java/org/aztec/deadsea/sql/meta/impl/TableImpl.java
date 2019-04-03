package org.aztec.deadsea.sql.meta.impl;

import org.aztec.deadsea.sql.meta.DataBase;
import org.aztec.deadsea.sql.meta.Location;
import org.aztec.deadsea.sql.meta.Table;

public class TableImpl extends BaseMetaData implements Table {
	
	DataBase db;

	public TableImpl() {
		// TODO Auto-generated constructor stub
	}

	public TableImpl(DataBase db,String name, String alias, Location location) {
		super(name, alias, location);
		this.db = db;
	}

	public DataBase getDataBase() {
		return db;
	}

}
