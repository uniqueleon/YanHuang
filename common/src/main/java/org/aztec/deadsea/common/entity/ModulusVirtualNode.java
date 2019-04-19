package org.aztec.deadsea.common.entity;

import java.util.List;

import org.aztec.deadsea.common.Route;
import org.aztec.deadsea.common.ShardData;
import org.aztec.deadsea.common.VirtualServer;

import com.beust.jcommander.internal.Lists;

public class ModulusVirtualNode implements VirtualServer {
	
	private Long modulus;
	private Long[] ranges;

	public ModulusVirtualNode(Long modulus,Long[] ranges) {
		// TODO Auto-generated constructor stub
	}

	public List<ShardData> find(Route route) {
		List<ShardData> dataList = Lists.newArrayList();
		List<ShardData> datas = route.getDatas();
		for(ShardData data : datas) {
			Long rem = data.getID().longValue() % modulus;
			if( rem > ranges[0] && rem > ranges[1]) {
				dataList.add(data);
			}
		}
		return dataList;
	}

}
