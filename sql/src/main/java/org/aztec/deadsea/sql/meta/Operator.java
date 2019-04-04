package org.aztec.deadsea.sql.meta;

public class Operator {
	
	private boolean isFunction;
	private boolean arithmetic;
	private Function function;
	private SimpleOperator operator;
	private LogisticConnector connector;
	
	public Operator(boolean function, boolean arithmetic, SimpleOperator operator, LogisticConnector connector) {
		super();
		this.isFunction = function;
		this.arithmetic = arithmetic;
		this.operator = operator;
		this.connector = connector;
	}

	
	public boolean isFunction() {
		return isFunction;
	}


	public void setFunction(boolean isFunction) {
		this.isFunction = isFunction;
	}


	public boolean isArithmetic() {
		return arithmetic;
	}

	public void setArithmetic(boolean arithmetic) {
		this.arithmetic = arithmetic;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public SimpleOperator getOperator() {
		return operator;
	}

	public void setOperator(SimpleOperator operator) {
		this.operator = operator;
	}

	public LogisticConnector getConnector() {
		return connector;
	}

	public void setConnector(LogisticConnector connector) {
		this.connector = connector;
	}

	public static enum SimpleOperator{
		L("<"),G(">"),LE("<="),
		GE(">="),EQUAL("="),BETWEEN("BETWEEN"),
		IN("IN"),NOT_IN("NOT IN"),
		IS_NULL("IS NULL"),NOT_NULL("NULL"),
		LIKE("LIKE"),NOT_LIKE("NOT LIKE");
		
		private String keyword;

		public String getKeyword() {
			return keyword;
		}

		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

		private SimpleOperator(String keyword) {
			this.keyword = keyword;
		}
		
	}
	
	public static enum LogisticConnector{
		AND("AND"),OR("OR");
		
		private String keyword;

		public String getKeyword() {
			return keyword;
		}

		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

		private LogisticConnector(String keyword) {
			this.keyword = keyword;
		}
		
		
	}
	
	
}
