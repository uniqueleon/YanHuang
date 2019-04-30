package org.aztec.deadsea.metacenter;

import java.util.List;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.entity.RealServerType;
import org.aztec.deadsea.common.entity.SimpleRealServer;
import org.aztec.deadsea.metacenter.impl.ZookeeperRegister;

import com.beust.jcommander.internal.Lists;

public class RegisterTest {

	public RegisterTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void testAddAuth() {

		try {
			MetaDataRegister register = new ZookeeperRegister();
			register.addauth("liming", "lm1234");
			//register.auth("liming", "lm1234");
		} catch (DeadSeaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testAuth() {
		try {
			MetaDataRegister register = new ZookeeperRegister();
			Authentication auth = register.auth("liming", "lm1234");
			System.out.println(auth.isAuthenticated());
		} catch (DeadSeaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void registServer() {
		try {
			ServerRegister register = new ZookeeperRegister();
			List<RealServer> newServers = Lists.newArrayList();
			newServers.add(new SimpleRealServer("db1.aztec.com", 0, 3306, RealServerType.SOCKET));
			register.regist(newServers);
		} catch (DeadSeaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//testAddAuth();
		//testAuth();
		registServer();
		System.exit(0);
	}

}
