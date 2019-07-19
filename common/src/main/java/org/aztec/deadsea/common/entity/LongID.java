package org.aztec.deadsea.common.entity;

import java.math.BigDecimal;

import org.aztec.deadsea.common.DataID;

public class LongID implements DataID {
	
	private long value;

	public LongID(long id) {
		this.value = id;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public BigDecimal getBigNumber() {
		return new BigDecimal(value);
	}

}
