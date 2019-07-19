package org.aztec.deadsea.common.entity;

import java.util.List;

import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.RoutingInfo;
import org.aztec.deadsea.common.ShardData;
import org.aztec.deadsea.common.VirtualServer;

import com.beust.jcommander.internal.Lists;

public class ModulusVirtualNode implements VirtualServer {
	
	private RealServer location;
	private Long modulus;
	private Long[] ranges;

	public ModulusVirtualNode(RealServer location,Long modulus,Long[] ranges) {
		// TODO Auto-generated constructor stub
		this.modulus = modulus;
		this.ranges = ranges;
		this.location = location;
	}
	
	public ModulusVirtualNode(Long modulus,Long[] ranges) {
		// TODO Auto-generated constructor stub
		this.modulus = modulus;
		this.ranges = ranges;
	}

	public List<ShardData> route(RoutingInfo route) {
		List<ShardData> dataList = Lists.newArrayList();
		List<ShardData> datas = route.getDatas();
		for(ShardData data : datas) {
			Long rem = data.getID().longValue() % modulus;
			if( rem >= ranges[0] && rem < ranges[1]) {
				dataList.add(data);
			}
		}
		return dataList;
	}

	public Long getModulus() {
		return modulus;
	}

	public void setModulus(Long modulus) {
		this.modulus = modulus;
	}

	public Long[] getRanges() {
		return ranges;
	}

	public void setRanges(Long[] ranges) {
		this.ranges = ranges;
	}

	@Override
	public <T> T cast(Class<T> castClass) {
		if(ModulusVirtualNode.class.isAssignableFrom(castClass)) {
			return (T) this;
		}
		return null;
	}

	@Override
	public RealServer getLocation() {
		return location;
	}

	public void setLocation(RealServer location) {
		this.location = location;
	}

}
