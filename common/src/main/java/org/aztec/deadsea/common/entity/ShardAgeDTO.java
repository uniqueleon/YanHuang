package org.aztec.deadsea.common.entity;

import org.aztec.deadsea.common.ShardingAge;

public class ShardAgeDTO extends BaseMetaData implements ShardingAge{

	private Long valve;
	private Long lastValve;
	
	public ShardAgeDTO(int no,int size,Long valve,Long lastValve) {
		super(no, "", size, true);
		this.valve = valve;
		this.lastValve = lastValve;
	}

	public Long getValve() {
		return valve;
	}

	public void setValve(Long valve) {
		this.valve = valve;
	}

	public Long getLastValve() {
		return lastValve;
	}

	public void setLastValve(Long lastValve) {
		this.lastValve = lastValve;
	}

	@Override
	public Long valve() {
		// TODO Auto-generated method stub
		return valve;
	}

	@Override
	public Long lastValve() {
		// TODO Auto-generated method stub
		return lastValve;
	}

	@Override
	public Integer getNo() {
		return no;
	}

	
}
