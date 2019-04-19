package org.aztec.deadsea.common.impl;

import java.util.concurrent.atomic.AtomicLong;

import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.deadsea.common.ShardingInfoCalculator;

public abstract class BaseDBCalculator implements ShardingInfoCalculator {
	
	protected final AtomicLong currentSize = new AtomicLong(0);
	protected final AtomicLong databaseSize = new AtomicLong(0);
	protected final AtomicLong tableSize = new AtomicLong(0);
	protected final AtomicLong realSize = new AtomicLong(0);
	protected final AtomicLong currentValve = new AtomicLong(0);

	public BaseDBCalculator(int currentSize,int realSize,int databaseSize,int tableSize) {
		this.currentSize.set(currentSize);
		this.databaseSize.set(databaseSize);
		this.tableSize.set(tableSize);
		this.realSize.set(realSize);
	}

	public long getRealSize() {
		return realSize.get();
	}

	public long getNextRealSize() {
		if(currentSize.get() == 1) {
			return 2;
		}
		long nextSize = realSize.get() + 1;
		if(isRelativelyPrime(nextSize, databaseSize.get(), tableSize.get())) {
			return nextSize;
		}
		else {
			nextSize ++;
			if(isRelativelyPrime(nextSize, databaseSize.get(), tableSize.get())) {
				return nextSize;
			}
			else {
				nextSize ++;
				return nextSize;
			}
		}
	}

	public long getCurrentSize() {
		return currentSize.get();
	}


	public long getNextSize() {
		return getNextRealSize() * currentSize.get();
	}
	
	public boolean isRelativelyPrime(long curSize,long dbSize,long tableSize) {
		return (GreatestCommonDivisor.calculate(curSize, dbSize).getGcd() == 1)
				&& (GreatestCommonDivisor.calculate(curSize, tableSize).getGcd() == 1);
	}

}
