package org.aztec.deadsea.sql.conf;

import java.util.List;

import com.google.common.collect.Lists;

public class DatabaseScheme {
	
	private String name;
	private int size;
	private List<TableScheme> tables;
	
	public DatabaseScheme(String name, int size) {
		super();
		this.name = name;
		this.size = size;
		tables = Lists.newArrayList();
	}
	public String getName() {
		return name;
	}
	public int size() {
		return size;
	}
	public List<TableScheme> getTables() {
		return tables;
	}
	public void setTables(List<TableScheme> tables) {
		this.tables = tables;
	}
	
}
