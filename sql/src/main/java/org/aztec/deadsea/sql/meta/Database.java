package org.aztec.deadsea.sql.meta;

public class Database{
	
	private String name;
	private String alias;
	private String charset;
	private String collate;

	public Database() {
		// TODO Auto-generated constructor stub
	}
	
	

	public Database(String name, String alias, String charset, String collate) {
		super();
		this.name = name;
		this.alias = alias;
		this.charset = charset;
		this.collate = collate;
	}



	public String name() {
		return name;
	}

	public String alias() {
		return alias;
	}

	public String charset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String collate() {
		return collate;
	}

	public void setCollate(String collate) {
		this.collate = collate;
	}
	
	

}
