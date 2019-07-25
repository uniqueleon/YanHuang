package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.AbstractSubNodeReloader;
import org.aztec.autumn.common.zk.CallableWatcher;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.VirtualServer;
import org.aztec.deadsea.common.entity.ModulusVirtualNode;
import org.aztec.deadsea.common.entity.RealServerType;
import org.aztec.deadsea.common.entity.SimpleRealServer;
import org.aztec.deadsea.metacenter.MetaCenterConst;

import com.beust.jcommander.internal.Lists;

public class RealServerInfo extends ZkConfig {

	private Integer no;
	private String host;
	private Integer port;
	private Integer proxyPort;
	private Integer age;
	@Ignored
	private List<VirtualNodeInfo> nodes;
	@Ignored
	private List<TimeLimitedCallable>  callBacks;
	@Ignored
	private VirtualNodeReloader loader;
	
	public RealServerInfo(Integer no, Integer age)
			throws Exception {
		super(String.format(MetaCenterConst.ZkConfigPaths.REAL_SERVER_INFO, new Object[] {age,no}), ConfigFormat.JSON);
		this.no = no;
		this.age = age;
		initRealServer();
	}


	public RealServerInfo(int age,int no, String host, Integer port, Integer proxyPort) throws Exception {
		super(String.format(MetaCenterConst.ZkConfigPaths.REAL_SERVER_INFO, new Object[] {age,no}), ConfigFormat.JSON);
		this.host = host;
		this.port = port;
		this.proxyPort = proxyPort;
		this.no = no;
		this.age = age;
		initRealServer();
	}
	
	private void initRealServer() throws Exception {
		if(isDeprecated)
			return;
		callBacks = Lists.newArrayList();
		VirtualNodeReloader loader = new VirtualNodeReloader(this);
		loader.load();
		callBacks.add(new VirtualNodeReloader(this));
		appendWatcher(new CallableWatcher(callBacks, null));
	}
	
	public class VirtualNodeReloader extends AbstractSubNodeReloader{

		public VirtualNodeReloader(ZkConfig parent) {
			super(parent);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Long getTime() {
			BaseInfo baseInfo = BaseInfo.getInstance();
			return baseInfo.getLoadTableTimeout();
		}

		@Override
		protected ZkConfig loadChild(int index) throws Exception {
			return new VirtualNodeInfo(znode, index);
		}

		@Override
		protected void setChildrens(List children) throws Exception {
			// TODO Auto-generated method stub
			nodes = children;
		}
		
	}


	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public RealServer toMetaData() {
		SimpleRealServer srs = new SimpleRealServer(age,host,no,port,RealServerType.SOCKET);
		if(proxyPort != null) {
			srs.setProxyPort(proxyPort);
		}
		List<VirtualServer> vs = Lists.newArrayList();
		for(VirtualNodeInfo node : nodes) {
			vs.add(node.toMetaData());
		}
		srs.setNodes(vs);
		return srs;
	}


	public List<VirtualNodeInfo> getNodes() {
		return nodes;
	}


	public void setNodes(List<VirtualNodeInfo> nodes) {
		this.nodes = nodes;
	}


	public void setVirtualServerInfo(List<VirtualServer> nodes2) throws IOException, KeeperException, InterruptedException {
		nodes = Lists.newArrayList();
		for(int i = 0;i < nodes2.size();i++) {
			VirtualServer vs = nodes2.get(i);
			if(vs instanceof ModulusVirtualNode) {
				ModulusVirtualNode mvn = (ModulusVirtualNode) vs;
				VirtualNodeInfo vni = new VirtualNodeInfo(znode, i,mvn.getModulus(),mvn.getRanges()[0],
						mvn.getRanges()[1]);
				nodes.add(vni);
			}
		}
	}
	
	public void save(boolean cascade) throws Exception {
		save();
		try {
			if(cascade) {
				for(VirtualNodeInfo vn : nodes) {
					vn.save();
				}
			}
		} catch (Exception e) {
			delete();
		}
	}

}
