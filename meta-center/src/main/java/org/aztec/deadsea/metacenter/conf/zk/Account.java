package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.metacenter.MetaCenterConst;

public class Account extends ZkConfig{
	
	private String username;
	private String password;
	private String uuid;
	@Ignored
	private List<TimeLimitedCallable>  callBacks;
	@Ignored
	private List<DatabaseInfo> databases;

	public Account(String uuid) throws IOException, KeeperException, InterruptedException {
		// TODO Auto-generated constructor stub
		super(String.format(MetaCenterConst.ZkConfigPaths.BASE_AUTHENTICATIONS_INFO, new Object[] {uuid}), ConfigFormat.JSON);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	

	
}
