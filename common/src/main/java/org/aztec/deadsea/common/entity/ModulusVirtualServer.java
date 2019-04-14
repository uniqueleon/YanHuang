package org.aztec.deadsea.common.entity;

import java.util.List;

import org.aztec.deadsea.common.DataID;
import org.aztec.deadsea.common.Route;
import org.aztec.deadsea.common.ShardData;
import org.aztec.deadsea.common.VirtualServer;

public class ModulusVirtualServer implements VirtualServer{
	
	private int modulus;
	private int remainder;

	public ModulusVirtualServer() {
		// TODO Auto-generated constructor stub
	}


	public int getModulus() {
		return modulus;
	}


	public void setModulus(int modulus) {
		this.modulus = modulus;
	}


	public int getRemainder() {
		return remainder;
	}

	public void setRemainder(int remainder) {
		this.remainder = remainder;
	}


	public boolean accept(Route route) {
		List<ShardData> shardDatas = route.getDatas();
		for(ShardData shardData : shardDatas) {
			DataID id = shardData.getID();
			long idRemainder = id.longValue() % modulus;
			return true;
		}
		
		return false;
	}


	
}
