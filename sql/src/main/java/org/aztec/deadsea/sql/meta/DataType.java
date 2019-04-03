package org.aztec.deadsea.sql.meta;

public enum DataType {

	INT("int"),BIGINT("bigint"),VARCHAR("varchar"),CHAR("char");
	
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	private DataType(String keyword) {
		this.keyword = keyword;
	}
	
	
	
}
