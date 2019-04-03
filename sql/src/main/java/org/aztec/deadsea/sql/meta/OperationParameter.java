package org.aztec.deadsea.sql.meta;

public interface OperationParameter {

	public boolean isParameterized();
	public <T> T get();
	public boolean isNumber();
	public boolean isString();
}
