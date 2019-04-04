package org.aztec.deadsea.sql.scheme;

public class TableScheme {
	
	private String name;
	private int size;
	private boolean shard;
	private DatabaseScheme database;
	
	public TableScheme() {
		
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
}
