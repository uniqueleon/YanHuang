package org.aztec.deadsea.metacenter.utils;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.autumn.common.zk.ZkConfig.ConfigFormat;
import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.common.VirtualServer;
import org.aztec.deadsea.common.entity.ModulusVirtualNode;
import org.aztec.deadsea.common.entity.RealServerType;
import org.aztec.deadsea.common.entity.ServerAgeDTO;
import org.aztec.deadsea.common.entity.SimpleRealServer;
import org.aztec.deadsea.metacenter.MetaDataException;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;
import org.aztec.deadsea.metacenter.conf.BaseConfDefaultValues;
import org.aztec.deadsea.metacenter.conf.zk.BaseInfo;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;


@SpringBootConfiguration
@ComponentScan(basePackages= {"org.aztec.deadsea.metacenter.impl"})
@Component
public class MetaDataInitiator implements ApplicationRunner {
	@Resource
	private ServerRegister register;
	@Resource
	private MetaDataRegister metaRegister;

	public MetaDataInitiator() {
		// TODO Auto-generated constructor stub
	}
	
	public void registBaseInfo() throws Exception {
		BaseInfo baseInfo = BaseInfo.getInstance();
		if(baseInfo.isDeprecated()) {
			baseInfo.setTableNum(BaseConfDefaultValues.TABLE_NUM);
			baseInfo.setRealNum(BaseConfDefaultValues.INIT_SERVER_NUM);
			baseInfo.setVirtualNum(BaseConfDefaultValues.INIT_VIRTUAL_SERVER_NUM);
			baseInfo.setTableSize(BaseConfDefaultValues.TABLE_SIZE);
			baseInfo.setLoadAgesTimeout(BaseConfDefaultValues.LOAD_AGE_TIME_OUT);
			baseInfo.setLoadServerTimeout(BaseConfDefaultValues.LOAD_SERVER_TIME_OUT);
			baseInfo.setLoadTableTimeout(BaseConfDefaultValues.LOAD_TABLE_TIME_OUT);
			baseInfo.setType(BaseConfDefaultValues.SERVER_TYPES[0]);
			baseInfo.setMaxAge(0);
			baseInfo.save();
		}
	}
	
	public Authentication addAuth(String username,String password) throws DeadSeaException {
		Authentication auth = null;
		try {
			auth = metaRegister.addauth(username, password);
		} catch (Exception e) {
			if(e instanceof MetaDataException) {
				MetaDataException ex = (MetaDataException) e;
				if(ex.getCode() == ErrorCodes.AUTHENTICATION_DUPLICATE_ERROR) {
					auth = metaRegister.auth(username, password);
				}
			}
			else {
				throw e;
			}
		}
		return auth;
	}
	
	public void registServer(Authentication auth,String host,Integer port,Integer proxyPort) throws DeadSeaException{

		try {
			List<VirtualServer> vServers = Lists.newArrayList();
			ModulusVirtualNode mvNode = new ModulusVirtualNode(1l,new Long[] {0l,1l});
			vServers.add(mvNode);
			ShardingAge age = new ServerAgeDTO(0, "AGE_0", 1, 0, 1000000);
			List<RealServer> realServers = Lists.newArrayList();
			SimpleRealServer sRealServer = new SimpleRealServer(age.getNo(), host, 0,port, RealServerType.SOCKET);
			sRealServer.setNodes(vServers);
			realServers.add(sRealServer);
			register.regist(auth, age, realServers);
		} catch (Exception e) {
			throw new DeadSeaException(e.getMessage(), ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}
	

	public void initAll(String username,String password,String host, Integer port, Integer proxyPort
			) throws MetaDataException {
		try {
			registBaseInfo();
			Authentication auth = addAuth(username, password);
			registServer(auth, host, port, proxyPort);
		} catch (Exception e) {
			throw new MetaDataException(MetaDataException.ErrorCodes.BASE_INFO_INIT_ERROR);
		}
	}

	public static void main(String[] args) {
		try {
			SpringApplication.run(MetaDataInitiator.class, args);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void run(ApplicationArguments appArgs) throws Exception {
		// TODO Auto-generated method stub
		String[] args = appArgs.getSourceArgs();
		if (args.length < 5) {
			System.err.println(" Should provide 5 parameters at least!");
			System.exit(0);
		}
		initAll(args[0], args[1],args[2], Integer.parseInt(args[3]),Integer.parseInt(args[4]));
	}
}
