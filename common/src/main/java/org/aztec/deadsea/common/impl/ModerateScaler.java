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
 * 
 * @author liming
 *
 */
public class ModerateScaler extends BaseDBScaler {

	public ModerateScaler(Long dataSize, long nodeSize, long realSize, long databaseSize, long tableSize) {
		super(dataSize, nodeSize, realSize, databaseSize, tableSize);
	}
	
	public ModerateScaler(Long dataSize, long databaseSize, long tableSize) {
		super(dataSize, 1l, 1l, databaseSize, tableSize);
	}

	// n' = n * (s + ds) / s * (ds - 1)
	public long getBalanceValve() {
		long nextSize = (dataSize.get() * (getCurrentSize() + getNextSize()))
				/ (getCurrentSize() * (getNextSize() - 1));
		return nextSize;
	}

	@Override
	public List<RealServer> reallocate4Inc(List<RealServer> realServers, boolean force) throws DeadSeaException {
		// 获取下次建议的实际服务器大小
		long expectedSize = getNextActualSize();
		int newSize = realServers.size();
		long modulus = 1l;
		if (expectedSize != realServers.size() && !force) {
			throw new DeadSeaException(GeneralErrors.NEW_SERVER_SIZE_INCORRECT);
		} else {
			modulus = newSize * getCurrentSize();
			if (!isRelativelyPrime(expectedSize, databaseSize.get(), tableSize.get())) {
				modulus = 2 * getCurrentSize();
			}
		}
		List<VirtualServer> virtualNodes = Lists.newArrayList();
		// 计算实际增加的服务器数量
		Long actualInc = newSize - getActualSize();
		Long currentModulus = getCurrentSize();
		// 将模数减去当前的模数除以增加数，得到新服务器的平均分片区间
		Long slice = (modulus - currentModulus) / actualInc;
		Long cursor = currentModulus;
		for (Long i = 0l; i < actualInc; i++) {
			virtualNodes.add(new ModulusVirtualNode(cursor, new Long[] { cursor, cursor + slice - 1 }));
			cursor += slice;
		}

		List<RealServer> sortedServer = Lists.newArrayList();
		for (RealServer server : realServers) {
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
		for (int i = 0; i < sortedServer.size(); i++) {
			RealServer server = sortedServer.get(i);
			if (!server.isNew()) {
				// 如果不是新加入结点，只更新模数，原来分配的区域不变，
				List<VirtualServer> nodes = server.getNodes();
				for (VirtualServer node : nodes) {
					ModulusVirtualNode mvNode = node.cast(ModulusVirtualNode.class);
					if (mvNode == null) {
						throw new DeadSeaException(GeneralErrors.UNSUPPORTED_OPERATION_PARAMETER);
					}
					mvNode.setModulus(modulus);
					mvNode.setLocation(server);
				}
			} else {
				// 如果是新加入的结点，则把增加的虚拟服务器节点加到新加入的结点中
				List<VirtualServer> nodes = Lists.newArrayList();
				VirtualServer mvNode = virtualNodes.get(nodeCursor);
				nodes.add(mvNode);
				mvNode.setLocation(server);
				server.setNodes(nodes);
				nodeCursor++;
			}
		}
		return sortedServer;
	}

	@Override
	public List<RealServer> reallocate4Dec(List<RealServer> servers, boolean force) throws DeadSeaException {
		throw new DeadSeaException(GeneralErrors.UNSUPPORTED_OPERATION);
	}

	@Override
	public List<RealServer> rebalance(List<RealServer> servers) throws DeadSeaException {
		// TODO Auto-generated method stub
		return null;
	}
}
