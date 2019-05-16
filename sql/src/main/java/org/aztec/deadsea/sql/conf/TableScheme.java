package org.aztec.deadsea.sql.conf;

public class TableScheme {
	
	private String name;
	private String alias;
	private int size;
	private boolean shard;
	private DatabaseScheme database;
	
	public TableScheme() {
		
	}

	public TableScheme(String name, String alias, int size, boolean shard, DatabaseScheme database) {
		super();
		this.name = name;
		this.alias = alias;
		this.size = size;
		this.shard = shard;
		this.database = database;
	}



	public String getName() {
		return name;
	}
	public int size() {
		return size;
	}
	public boolean isShard() {
		return shard;
	}

	public DatabaseScheme getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseScheme database) {
		this.database = database;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setShard(boolean shard) {
		this.shard = shard;
	}
	
	
}
