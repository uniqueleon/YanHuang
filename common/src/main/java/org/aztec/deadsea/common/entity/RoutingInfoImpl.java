package org.aztec.deadsea.common.entity;

import java.util.List;

import org.aztec.deadsea.common.RoutingInfo;
import org.aztec.deadsea.common.ShardData;

public class RoutingInfoImpl implements RoutingInfo {
	
	private List<ShardData> datas;

	public RoutingInfoImpl(List<ShardData> datas) {
		this.datas = datas;
	}

	@Override
	public List<ShardData> getDatas() {
		return datas;
	}

}
