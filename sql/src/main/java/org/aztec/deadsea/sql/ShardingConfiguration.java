package org.aztec.deadsea.sql;

public interface ShardingConfiguration {

	public int getTableNum();
	public int getDataBaseNum();
	public int getVirtualServerNum();
	public int getRealServerNum();
}
