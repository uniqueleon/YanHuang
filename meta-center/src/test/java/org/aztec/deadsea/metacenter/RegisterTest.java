package org.aztec.deadsea.metacenter;

import java.util.List;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.VirtualServer;
import org.aztec.deadsea.common.entity.DatabaseDTO;
import org.aztec.deadsea.common.entity.ModulusVirtualNode;
import org.aztec.deadsea.common.entity.RealServerType;
import org.aztec.deadsea.common.entity.SimpleRealServer;
import org.aztec.deadsea.metacenter.conf.BaseConfDefaultValues;
import org.aztec.deadsea.metacenter.conf.zk.BaseInfo;
import org.aztec.deadsea.metacenter.conf.zk.ServerAge;
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testAuth() {
		try {
			MetaDataRegister register = new ZookeeperRegister();
			Authentication auth = register.auth("liming", "lm1234");
			System.out.println(auth.isAuthenticated());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void registServer() {
		try {
			ZookeeperRegister register = new ZookeeperRegister();
			ServerRegister sr = register;
			Authentication auth = register.auth("liming", "lm1234");
			List<RealServer> newServers = Lists.newArrayList();
			RealServer rs = new SimpleRealServer(0,"db1.aztec.com", 0, 3306, RealServerType.SOCKET);
			List<VirtualServer> mvn = Lists.newArrayList();
			mvn.add(new ModulusVirtualNode(1l,new Long[] {0l,1l}));
			rs.setNodes(mvn);
			newServers.add(rs);
			ServerAge sa = new ServerAge(0,0l,1000000l);
			register.registServer(auth,sa.toMetaData(),newServers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void registDatabase() {
		try {
			
			ZookeeperRegister register = new ZookeeperRegister();
			Authentication auth = register.auth("liming", "lm1234");
			DatabaseDTO data = new DatabaseDTO(0, "lmDb", 11, 0, true, Lists.newArrayList());
			register.regist(auth, data);
			/*newServers.add(new SimpleRealServer("db1.aztec.com", 0, 3306, RealServerType.SOCKET));
			register.regist(auth,newServers);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateDatabase() {
		try {
			
			ZookeeperRegister register = new ZookeeperRegister();
			Authentication auth = register.auth("liming", "lm1234");
			DatabaseDTO data = new DatabaseDTO(0, "lmDb", 11, 0, true, Lists.newArrayList());
			register.update(auth, data);
			/*newServers.add(new SimpleRealServer("db1.aztec.com", 0, 3306, RealServerType.SOCKET));
			register.regist(auth,newServers);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void registBaseInfo()  {
		try {
			BaseInfo baseInfo = BaseInfo.getInstance();
			baseInfo.setTableNum(BaseConfDefaultValues.TABLE_NUM);
			baseInfo.setRealNum(BaseConfDefaultValues.INIT_SERVER_NUM);
			baseInfo.setVirtualNum(BaseConfDefaultValues.INIT_VIRTUAL_SERVER_NUM);
			baseInfo.setTableSize(BaseConfDefaultValues.TABLE_SIZE);
			baseInfo.setLoadAgesTimeout(BaseConfDefaultValues.LOAD_AGE_TIME_OUT);
			baseInfo.setLoadServerTimeout(BaseConfDefaultValues.LOAD_SERVER_TIME_OUT);
			baseInfo.setLoadTableTimeout(BaseConfDefaultValues.LOAD_TABLE_TIME_OUT);
			baseInfo.setType(BaseConfDefaultValues.SERVER_TYPES[0]);
			baseInfo.setGlobalAccessString("liming:lm1234");
			baseInfo.setMaxAge(0);
			baseInfo.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//testAddAuth();
		//testAuth();
		registServer();
		//registDatabase();
		//registBaseInfo();
		//updateDatabase();
		System.exit(0);
	}

}
