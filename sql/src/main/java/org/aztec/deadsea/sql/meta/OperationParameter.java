package org.aztec.deadsea.sql.meta;

public class OperationParameter {
	
	private boolean parameterized;
	private Object value;
	private boolean number;
	private boolean string;
	public boolean isParameterized() {
		return parameterized;
	}
	public void setParameterized(boolean parameterized) {
		this.parameterized = parameterized;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public boolean isNumber() {
		return number;
	}
	public void setNumber(boolean number) {
		this.number = number;
	}
	public boolean isString() {
		return string;
	}
	public void setString(boolean string) {
		this.string = string;
	}
	

}
