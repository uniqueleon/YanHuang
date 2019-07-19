package org.aztec.deadsea.common.impl;

import java.util.List;

import org.aztec.deadsea.common.RoutingInfo;
import org.aztec.deadsea.common.RoutingInfoBuilder;
import org.aztec.deadsea.common.ShardData;
import org.aztec.deadsea.common.entity.LongID;
import org.aztec.deadsea.common.entity.RoutingInfoImpl;
import org.aztec.deadsea.common.entity.ShardDataImpl;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;

@Component
public class DefaultRoutingInfoBuilder implements RoutingInfoBuilder{

	public DefaultRoutingInfoBuilder() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public RoutingInfo build(List<Long> ids) {
		List<ShardData> datas = Lists.newArrayList();
		for(Long id : ids) {
			datas.add(new ShardDataImpl(new LongID(id), null));
		}
		RoutingInfoImpl routeInfos = new RoutingInfoImpl(datas);
		return routeInfos;
	}

}
