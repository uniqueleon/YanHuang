package org.aztec.deadsea.sql.conf;

import java.util.List;

public class DatabaseScheme {
	
	private String name;
	private int size;
	private List<TableScheme> tables;
	private ServerScheme server;
	
	public DatabaseScheme(String name, int size) {
		super();
		this.name = name;
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public int size() {
		return size;
	}
	public ServerScheme getServer() {
		return server;
	}
	public void setServer(ServerScheme server) {
		this.server = server;
	}
	public List<TableScheme> getTables() {
		return tables;
	}
	public void setTables(List<TableScheme> tables) {
		this.tables = tables;
	}
	
}
