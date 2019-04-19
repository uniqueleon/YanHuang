package org.aztec.deadsea.common;

public interface ShardingInfoCalculator {

	public long getRealSize();
	public long getNextRealSize();
	public long getCurrentSize();
	public long getNextSize();
	public long getBalanceValve();
	public void update();
}
