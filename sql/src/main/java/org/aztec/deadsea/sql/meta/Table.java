package org.aztec.deadsea.sql.meta;public interface Table {

	public DataBase getDataBase();
	public String name();
	public String alias();
	public Location location();
}
