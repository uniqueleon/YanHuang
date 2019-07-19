package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.VirtualServer;
import org.aztec.deadsea.common.entity.ModulusVirtualNode;

public class VirtualNodeInfo extends ZkConfig{
	
	private String nodeType;
	private Long modulus;
	private Long lowerLimit;
	private Long upperLimit;

	public VirtualNodeInfo(String prefix,int no) throws IOException, KeeperException, InterruptedException {
		super(prefix + "." + no,ConfigFormat.JSON);
		nodeType = "modulus";
	}

	public VirtualNodeInfo(String prefix,int no, Long modulus, Long lowerLimit,
			Long upperLimit) throws IOException, KeeperException, InterruptedException {
		super(prefix + "." + no, ConfigFormat.JSON);
		this.nodeType = "modulus";
		this.modulus = modulus;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
	}



	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Long getModulus() {
		return modulus;
	}

	public void setModulus(Long modulus) {
		this.modulus = modulus;
	}

	public Long getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Long lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Long getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Long upperLimit) {
		this.upperLimit = upperLimit;
	}
	
	public VirtualServer toMetaData() {
		ModulusVirtualNode mvn = new ModulusVirtualNode(modulus, new Long[] {lowerLimit,upperLimit});
		return mvn;
	}
}
