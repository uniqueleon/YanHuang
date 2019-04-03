package org.aztec.deadsea.sql.meta;

public interface Operator {

	public boolean isFunction();
	public boolean isArithmetic();
	public Function getFunction();
	public SimpleOperator getSimpleOperator();
	public LogisticConnector getLogisticConnector();
	
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
