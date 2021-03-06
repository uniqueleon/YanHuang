package org.aztec.deadsea.sql.impl;

import java.util.List;
import java.util.Map;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.MetaDataRegister.MetaDataMapKeys;
import org.aztec.deadsea.common.RealServer;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.ServerRegistration;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.common.entity.DatabaseDTO;
import org.aztec.deadsea.common.entity.GlobalInfoDTO;
import org.aztec.deadsea.common.entity.ServerAgeDTO;
import org.aztec.deadsea.common.entity.TableDTO;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;
import org.aztec.deadsea.sql.Asserts;
import org.aztec.deadsea.sql.Asserts.CompareType;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.conf.AuthorityScheme;
import org.aztec.deadsea.sql.conf.LocalAuthConfiguration;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.meta.Database;
import org.aztec.deadsea.sql.meta.SqlMetaData;
import org.aztec.deadsea.sql.meta.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component(ShardingConfigurationFactory.DEFAULT_CONFIGURATION_BEAN_NAME)
@Scope("singleton")
public class MetaCenterConfiguration implements ShardingConfiguration {

	private MetaDataRegister registor;
	private ServerRegister serverRegistor;
	private LocalAuthConfiguration localConf;
	private Authentication auth;

	public MetaCenterConfiguration(MetaDataRegister registor,ServerRegister serverRegistor) throws DeadSeaException {
		// TODO Auto-generated constructor stub
		localConf = new LocalAuthConfiguration();
		this.registor = registor;
		this.serverRegistor = serverRegistor;
		auth = registor.auth(localConf.getUsername(), localConf.getPassword());
	}

	@Override
	public TableDTO getTargetTable(Table table) throws DeadSeaException {
		Map<String, List<MetaData>> metaDatas = registor.getRegistedMetaDatas(auth);
		List<MetaData> dbMetaDatas = metaDatas.get(MetaDataMapKeys.DATA_BASE_KEY);
		return findTable(dbMetaDatas, table);
	}


	public TableDTO findTable(List<MetaData> metaDataList, Table table) {
		for (MetaData mData : metaDataList) {
			DatabaseDTO db = mData.cast();
			
			List<MetaData> childrens = db.getChilds();
			if(childrens != null) {
				for (MetaData child : childrens) {
					TableDTO tableDto = child.cast();
					if (tableDto.getName().equals(table.name())) {
						return tableDto;
					}
				}
			}
		}
		return null;
	}

	@Override
	public List<ServerScheme> getRealServers(Integer age) throws DeadSeaException {
		Map<String, List<MetaData>> metaDatas = registor.getRegistedMetaDatas(auth);
		GlobalInfoDTO globalInfo = metaDatas.get(MetaDataRegister.MetaDataMapKeys.GLOBAL_INFORMATION).get(0).cast();
		List<MetaData> ages = metaDatas.get(MetaDataRegister.MetaDataMapKeys.SERVER_AGE);
		MetaData targetAge = age == null ? ages.get(ages.size() - 1) : ages.get(age);
		ShardingAge shardAge = targetAge.cast();
		ServerRegistration registration = serverRegistor.getServerRegistration(auth, shardAge);
		List<RealServer> realServers = registration.getAllServers();
		AuthorityScheme authScheme = getAuthScheme(globalInfo.getAccessString());
		List<ServerScheme> schemes = Lists.newArrayList();
		for (RealServer rServer : realServers) {
			ServerScheme serverScheme = new ServerScheme(rServer.getHost(), rServer.getPort(), rServer.getProxyPort(),
					authScheme);
			serverScheme.setNodes(rServer.getNodes());
			schemes.add(serverScheme);
		}
		return schemes;
	}

	public AuthorityScheme getAuthScheme(String accessString) {
		String[] splitString = accessString.split(":");
		AuthorityScheme authScheme = new AuthorityScheme(splitString[0], splitString[1]);
		Map<String, Object> props = Maps.newHashMap();
		if (splitString.length > 2) {
			for (int i = 2; i < splitString.length; i++) {
				String text = splitString[i];
				String[] keyValuePair = text.split("=");
				props.put(keyValuePair[0], keyValuePair[1]);
			}
		}
		authScheme.setParameters(props);
		return authScheme;
	}

	@Override
	public List<List<ServerScheme>> getAllRealServers() throws DeadSeaException {

		Map<String, List<MetaData>> metaDatas = registor.getRegistedMetaDatas(auth);
		GlobalInfoDTO globalInfo = metaDatas.get(MetaDataRegister.MetaDataMapKeys.GLOBAL_INFORMATION).get(0).cast();
		List<List<ServerScheme>> allSchemes = Lists.newArrayList();
		for(int i = 0;i < globalInfo.getMaxAge();i++) {
			List<ServerScheme> serverScheme = getRealServers(i);
			if(serverScheme != null) {
				allSchemes.add(serverScheme);
			}
		}
		return allSchemes;
	}

	@Override
	public ShardingAge getCurrentAge() throws DeadSeaException {
		//GlobalInfoDTO globalInfo = metaDatas.get(MetaDataRegister.MetaDataMapKeys.GLOBAL_INFORMATION).get(0).cast();

		Map<String, List<MetaData>> metaDatas = registor.getRegistedMetaDatas(auth);
		GlobalInfoDTO globalInfo = metaDatas.get(MetaDataRegister.MetaDataMapKeys.GLOBAL_INFORMATION).get(0).cast();
		List<MetaData> ages = metaDatas.get(MetaDataRegister.MetaDataMapKeys.SERVER_AGE);
		Asserts.assertNotNull(globalInfo, ErrorCodes.META_DATA_ERROR);
		Asserts.assertNotNull(ages, ErrorCodes.META_DATA_ERROR);
		Asserts.assertSize(ages, globalInfo.getMaxAge(), CompareType.GREATE, ErrorCodes.META_DATA_ERROR);
		ServerAgeDTO currentAge = ages.get(globalInfo.getMaxAge()).cast();
		return currentAge;
	}

	@Override
	public Authentication getAuth() throws DeadSeaException {
		return auth;
	}

	@Override
	public DatabaseDTO getDatabaseScheme(Database database) throws DeadSeaException {
		Map<String, List<MetaData>> metaDatas = registor.getRegistedMetaDatas(auth);
		List<MetaData> dbMetaDatas = metaDatas.get(MetaDataMapKeys.DATA_BASE_KEY);
		for(MetaData mData : dbMetaDatas) {
			DatabaseDTO dbDto = mData.cast();
			String dbName = dbDto.getName().replaceAll("`", "");
			if(dbName.equals(database.name().replaceAll("`", ""))) {
				return dbDto;
			}
		}
		return null;
	}

	public List<ServerScheme> getOperationTarget(SqlMetaData metaData) throws DeadSeaException {
		// TODO Auto-generated method stub
		ServerRegistration serverRegistration = serverRegistor.getServerRegistration(auth, getCurrentAge());
		//List<ServerScheme> serverScheme = 
		switch(metaData.getSqlType()) {
		case INSERT:
			 TableDTO insertTable = getTargetTable(metaData.getTable());
			 Long seqNo = insertTable.getRecordSeqNo();
			 
		case QUERY:
			
		case UPDATE:
			
		}
		return null;
	}

	@Override
	public GlobalInfoDTO getGlobal() throws DeadSeaException {
		return registor.getGlobalInfo();
	}

}
