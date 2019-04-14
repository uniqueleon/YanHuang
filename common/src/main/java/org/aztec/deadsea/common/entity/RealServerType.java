package org.aztec.deadsea.common.entity;

public enum RealServerType {

	
	
	HTTP("http"),SOCKET("socket");
	
	String code;

	private RealServerType(String code) {
		this.code = code;
	}
	
	
}
