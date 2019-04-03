package org.aztec.deadsea.sql.meta;

public interface Condition {

	public Column getColumn();
	public OperationParameter getParameter();
	public Operator getOperator();
}
