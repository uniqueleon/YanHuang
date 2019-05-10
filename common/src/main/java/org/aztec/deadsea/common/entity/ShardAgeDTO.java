package org.aztec.deadsea.common.entity;

public class ShardAgeDTO extends BaseMetaData{

	private Long valve;
	private Long LastValve;
	private Long modulus;
	
	public ShardAgeDTO(int no) {
		super(no, null, null, true);
	}

	public Long getValve() {
		return valve;
	}

	public void setValve(Long valve) {
		this.valve = valve;
	}

	public Long getModulus() {
		return modulus;
	}

	public void setModulus(Long modulus) {
		this.modulus = modulus;
	}

	public Long getLastValve() {
		return LastValve;
	}

	public void setLastValve(Long lastValve) {
		LastValve = lastValve;
	}

	
}
