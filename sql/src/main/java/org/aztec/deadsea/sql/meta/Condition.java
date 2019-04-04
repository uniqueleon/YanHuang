package org.aztec.deadsea.sql.meta;

public class Condition {

	private Column column;
	private OperationParameter parameter;
	private Operator operator;

	public Condition(Column column, OperationParameter parameter, Operator operator) {
		super();
		this.column = column;
		this.parameter = parameter;
		this.operator = operator;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public OperationParameter getParameter() {
		return parameter;
	}

	public void setParameter(OperationParameter parameter) {
		this.parameter = parameter;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

}
