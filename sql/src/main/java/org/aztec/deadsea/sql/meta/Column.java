package org.aztec.deadsea.sql.meta;

public interface Column {

	public Table table();
	public String name();
	public String alias();
	public Location location();
	
}
