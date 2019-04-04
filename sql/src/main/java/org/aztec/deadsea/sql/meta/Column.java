package org.aztec.deadsea.sql.meta;

public class Column extends BaseMetaData {
	private Table table;
	private DataType type;

	public Column(Table table, String name, String alias, Location location) {
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
