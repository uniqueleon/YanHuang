package org.aztec.deadsea.sql.meta;

public class OrderByClause {
	
	private Column column;
	private SequenceType type;
	public OrderByClause(Column column, SequenceType type) {
		super();
		this.column = column;
		this.type = type;
	}
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}
	public SequenceType getType() {
		return type;
	}
	public void setType(SequenceType type) {
		this.type = type;
	}
	
}
