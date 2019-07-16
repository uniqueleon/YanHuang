package org.aztec.deadsea.metacenter.utils;

import java.util.List;

import javax.annotation.Resource;

import org.aztec.autumn.common.utils.BasePropertiesConfig;
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
	
	public void registBaseInfo(InitiatorData initData) throws Exception {
		BaseInfo baseInfo = BaseInfo.getInstance();
		if(initData.getOverwrite() || baseInfo.isDeprecated()) {
			baseInfo.setTableNum(BaseConfDefaultValues.TABLE_NUM);
			baseInfo.setRealNum(BaseConfDefaultValues.INIT_SERVER_NUM);
			baseInfo.setVirtualNum(BaseConfDefaultValues.INIT_VIRTUAL_SERVER_NUM);
			baseInfo.setTableSize(BaseConfDefaultValues.TABLE_SIZE);
			baseInfo.setLoadAgesTimeout(BaseConfDefaultValues.LOAD_AGE_TIME_OUT);
			baseInfo.setLoadServerTimeout(BaseConfDefaultValues.LOAD_SERVER_TIME_OUT);
			baseInfo.setLoadTableTimeout(BaseConfDefaultValues.LOAD_TABLE_TIME_OUT);
			baseInfo.setType(BaseConfDefaultValues.SERVER_TYPES[0]);
			baseInfo.setGlobalAccessString(initData.getAccessString());
			baseInfo.setMaxAge(0);
			baseInfo.save();
		}
	}
	
	public Authentication addAuth(InitiatorData initData) throws DeadSeaException {
		Authentication auth = null;
		try {
			auth = metaRegister.addauth(initData.getAuthName(), initData.getPassword());
		} catch (Exception e) {
			if(e instanceof MetaDataException) {
				MetaDataException ex = (MetaDataException) e;
				if(ex.getCode() == ErrorCodes.AUTHENTICATION_DUPLICATE_ERROR) {
					auth = metaRegister.auth(initData.getAuthName(), initData.getPassword());
				}
			}
			else {
				throw e;
			}
		}
		return auth;
	}
	
	public void registServer(Authentication auth,InitiatorData initData) throws DeadSeaException{

		try {
			List<VirtualServer> vServers = Lists.newArrayList();
			ModulusVirtualNode mvNode = new ModulusVirtualNode(1l,new Long[] {0l,1l});
			vServers.add(mvNode);
			ShardingAge age = new ServerAgeDTO(0, "AGE_0", 1, 0, 1000000);
			List<RealServer> realServers = Lists.newArrayList();
			SimpleRealServer sRealServer = new SimpleRealServer(age.getNo(), initData.getHostName(), 0,initData.getPort(), RealServerType.SOCKET);
			sRealServer.setNodes(vServers);
			realServers.add(sRealServer);
			register.regist(auth, age, realServers);
		} catch (Exception e) {
			throw new DeadSeaException(e.getMessage(), ErrorCodes.META_DATA_PERSIT_ERROR);
		}
	}
	

	public void initAll(InitiatorData initData) throws MetaDataException {
		try {
			registBaseInfo(initData);
			Authentication auth = addAuth(initData);
			registServer(auth, initData);
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
	
	public class InitiatorData {
		private String serverType;
		private String authName;
		private String password;
		private String hostName;
		private Integer port;
		private Integer proxyPort;
		private String accessString;
		private Boolean overwrite;
		public InitiatorData(String[] args) {
			super();
			this.serverType = args[0];
			this.authName = args[0];
			this.password = args[1];
			this.hostName = args[2];
			this.port = Integer.parseInt(args[3]);
			this.proxyPort = Integer.parseInt(args[4]);
			this.accessString = args[5];
			overwrite = Boolean.parseBoolean(args[args.length - 1]);
		}
		public String getServerType() {
			return serverType;
		}
		public void setServerType(String serverType) {
			this.serverType = serverType;
		}
		public String getAuthName() {
			return authName;
		}
		public void setAuthName(String authName) {
			this.authName = authName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getHostName() {
			return hostName;
		}
		public void setHostName(String hostName) {
			this.hostName = hostName;
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
		public String getAccessString() {
			return accessString;
		}
		public void setAccessString(String accessString) {
			this.accessString = accessString;
		}
		public Boolean getOverwrite() {
			return overwrite;
		}
		public void setOverwrite(Boolean overwrite) {
			this.overwrite = overwrite;
		}
		
		
	}

	@Override
	public void run(ApplicationArguments appArgs) throws Exception {
		// TODO Auto-generated method stub
		System.setProperty(BasePropertiesConfig.DEFAUTL_SYSTEM_PROPERTY_FILE,"conf/deadsea_base.properties");
		String[] args = appArgs.getSourceArgs();
		if (args.length < 6) {
			System.err.println(" Should provide 6 parameters at least!");
			System.exit(0);
		}
		initAll(new InitiatorData(args));
	}
}
