package org.aztec.deadsea.sql.meta;

public class Column extends BaseMetaData {
	private Table table;
	private DataType type;
	private int no;

	public Column(Table table, String name, String alias, int no,Location location) {
		super(name, alias, location);
		this.table = table;
		this.no = no;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
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
		return type;
	}

}
