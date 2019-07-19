package org.aztec.deadsea.common.impl;

import java.util.concurrent.atomic.AtomicLong;

import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.deadsea.common.ServerScaler;

public abstract class BaseDBScaler implements ServerScaler {
	
	protected final AtomicLong currentSize = new AtomicLong(0);
	protected final AtomicLong databaseSize = new AtomicLong(0);
	protected final AtomicLong tableSize = new AtomicLong(0);
	protected final AtomicLong realSize = new AtomicLong(0);
	protected final AtomicLong nextRealSize = new AtomicLong(0);
	protected final AtomicLong currentValve = new AtomicLong(0);
	protected final AtomicLong dataSize = new AtomicLong(0);

	public BaseDBScaler(Long dataSize,long currentSize,long realSize,long databaseSize,long tableSize) {
		this.currentSize.set(currentSize);
		this.databaseSize.set(databaseSize);
		this.tableSize.set(tableSize);
		this.realSize.set(realSize);
		this.databaseSize.set(dataSize);
	}

	public long getActualSize() {
		return realSize.get();
	}

	public long getNextActualSize() {

		long nextRealSize = realSize.get() + 1;
		while(!isRelativelyPrime(nextRealSize, databaseSize.get(), tableSize.get())) {
			nextRealSize ++;
		}
		return nextRealSize;
	}

	public long getCurrentSize() {
		return currentSize.get();
	}



	public long getNextSize() {
		
		if(realSize.get() == 1) {
			return 4;
		}
		long realSize = getActualSize();
		long nextRealSize = getNextActualSize();
		long nextSize = realSize * nextRealSize;
		return nextSize;
	}
	
	public boolean isRelativelyPrime(long curSize,long dbSize,long tableSize) {
		return (GreatestCommonDivisor.calculate(curSize, dbSize).getGcd() == 1)
				&& (GreatestCommonDivisor.calculate(curSize, tableSize).getGcd() == 1);
	}

}
