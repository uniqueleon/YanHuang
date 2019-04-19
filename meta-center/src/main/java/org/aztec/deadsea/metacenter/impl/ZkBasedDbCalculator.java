package org.aztec.deadsea.metacenter.impl;

import org.aztec.deadsea.common.impl.BaseDBCalculator;
import org.aztec.deadsea.metacenter.conf.BaseInfo;

public class ZkBasedDbCalculator extends BaseDBCalculator {
	
	private BaseInfo baseInfo;

	public ZkBasedDbCalculator(BaseInfo baseInfo,int currentSize, int realSize, int databaseSize, int tableSize) {
		super(currentSize, realSize, databaseSize, tableSize);
	}

	public long getBalanceValve() {
		return 0;
	}

	public void update() {
		currentSize.set(baseInfo.getVirtualNum());
		realSize.set(baseInfo.getRealNum());
	}


}
