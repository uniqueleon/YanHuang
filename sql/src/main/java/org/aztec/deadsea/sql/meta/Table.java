package org.aztec.deadsea.sql.meta;

public class Table extends BaseMetaData  {
	
	Database db;
	

	public Table() {
		// TODO Auto-generated constructor stub
	}

	public Table(Database db,String name, String alias, Location location) {
		super(name, alias, location);
		this.db = db;
	}

	public Database getDatabase() {
		return db;
	}

}
