package org.aztec.deadsea.common.impl;

import java.util.Comparator;
import java.util.List;

import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.VirtualServer;
import org.aztec.deadsea.common.DeadSeaException.GeneralErrors;
import org.aztec.deadsea.common.entity.ModulusVirtualNode;

import com.google.common.collect.Lists;

/**
 * 相对温和的服务器扩容方案，即新旧服务器一起承担新数据的读写任务
 * @author liming
 *
 */
public class ModerateScaler extends BaseDBScaler {
	

	public ModerateScaler(Long dataSize,int currentSize, int realSize, int databaseSize, int tableSize) {
		super(dataSize,currentSize, realSize, databaseSize, tableSize);
	}

	// n' = n * (s + ds) / s * (ds - 1)
	public long getBalanceValve() {
		long nextSize = (dataSize.get() * (getCurrentSize() + getNextSize())) 
				/ (getCurrentSize() * (getNextSize() - 1));
		return nextSize;
	}

	@Override
	public List<RealServer> reallocate4Inc(List<RealServer> realServers,boolean force) throws DeadSeaException {
		long realSize = getNextActualSize();
		int actualSize = realServers.size();
		if(realSize != realServers.size() && !force) {
			throw new DeadSeaException(GeneralErrors.NEW_SERVER_SIZE_INCORRECT);
		}
		else {
			realSize = actualSize * getCurrentSize();
			if(!isRelativelyPrime(realSize, databaseSize.get(), tableSize.get())) {
				realSize = 2 * getCurrentSize();
			}
		}
		List<VirtualServer> virtualNodes = Lists.newArrayList();
		Long actualInc = actualSize - getActualSize();
		Long modulus = realSize;
		Long slice = (modulus - realSize ) / actualInc;
		Long cursor = modulus;
		for(Long i = 0l;i < actualInc;i++) {
			virtualNodes.add(new ModulusVirtualNode(cursor, new Long[] {cursor , cursor + slice  - 1}));
			cursor += slice;
		}
		
		List<RealServer> sortedServer = Lists.newArrayList();
		for(RealServer server : realServers) {
			sortedServer.add(server.cloneThis());
		}
		sortedServer.sort(new Comparator<RealServer>() {

			@Override
			public int compare(RealServer o1, RealServer o2) {
				// TODO Auto-generated method stub
				return o2.getNo() - o1.getNo();
			}
		});
		int nodeCursor = 0;
		for(int i = 0;i < sortedServer.size();i++) {
			RealServer server = sortedServer.get(i);
			if(!server.isNew()) {
				List<VirtualServer> nodes = server.getNodes();
				for(VirtualServer node : nodes) {
					ModulusVirtualNode mvNode = node.cast(ModulusVirtualNode.class);
					if(mvNode == null) {
						throw new DeadSeaException(GeneralErrors.UNSUPPORTED_OPERATION_PARAMETER);
					}
					mvNode.setModulus(modulus);
				}
			}
			else {
				List<VirtualServer> nodes = Lists.newArrayList();
				nodes.add(virtualNodes.get(nodeCursor));
				sortedServer.get(i).setNodes(nodes);
				nodeCursor ++;
			}
		}
		return sortedServer;
	}
	

	@Override
	public List<RealServer> reallocate4Dec(List<RealServer> servers, boolean force) throws DeadSeaException {
		throw new DeadSeaException(GeneralErrors.UNSUPPORTED_OPERATION);
	}
}
