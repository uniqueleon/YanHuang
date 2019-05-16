package org.aztec.deadsea.common.entity;

import org.aztec.deadsea.common.ShardingAge;

public class ServerAgeDTO extends BaseMetaData implements ShardingAge {
	
	private Long valve;
	private Long lastValve;

	public ServerAgeDTO(String name, Integer size) {
		super(name, size, true);
	}

	public ServerAgeDTO(int no, String name, Integer size,long valve,long lastValve) {
		super(no, name, size, true);
		this.valve = lastValve;
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
		return valve;
	}

	@Override
	public Long lastValve() {
		return lastValve;
	}

	@Override
	public Integer getNo() {
		return no;
	}

}
