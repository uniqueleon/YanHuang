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
import org.aztec.deadsea.common.entity.TableDTO;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.conf.AuthorityScheme;
import org.aztec.deadsea.sql.conf.DatabaseScheme;
import org.aztec.deadsea.sql.conf.LocalAuthConfiguration;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.conf.TableScheme;
import org.aztec.deadsea.sql.meta.Table;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MetaCenterConfiguration implements ShardingConfiguration {

	private MetaDataRegister registor;
	private ServerRegister serverRegistor;
	private LocalAuthConfiguration localConf;
	private Authentication auth;

	public MetaCenterConfiguration(MetaDataRegister registor, ServerRegister serverRegistor) throws DeadSeaException {
		// TODO Auto-generated constructor stub
		localConf = new LocalAuthConfiguration();
		auth = registor.auth(localConf.getUsername(), localConf.getPassword());
		this.registor = registor;
		this.serverRegistor = serverRegistor;
	}

	@Override
	public TableScheme getTargetTable(Table table) throws DeadSeaException {
		Map<String, List<MetaData>> metaDatas = registor.getRegistedMetaDatas(auth);
		List<MetaData> dbMetaDatas = metaDatas.get(MetaDataMapKeys.DATA_BASE_KEY);
		DatabaseDTO dbDTO = findMatchDatabase(dbMetaDatas, table);
		TableScheme ts = new TableScheme();
		return null;
	}

	private TableScheme wrapTableScheme(DatabaseDTO dbDTO, Table table) {
		DatabaseScheme dbScheme = new DatabaseScheme(dbDTO.getName(), dbDTO.getSize());
		TableScheme targetScheme = null;
		List<TableScheme> schemes = Lists.newArrayList();
		for (MetaData tableMetaData : dbDTO.getChilds()) {
			TableDTO tableDto = tableMetaData.cast();
			TableScheme tableScheme = new TableScheme(tableDto.getName(), table.alias(), tableDto.getSize(),
					tableDto.shard(), dbScheme);
			if (tableDto.getName().equals(table.name())) {
				targetScheme = tableScheme;
			}
			schemes.add(tableScheme);
		}
		return targetScheme;
	}

	public DatabaseDTO findMatchDatabase(List<MetaData> metaDataList, Table table) {
		for (MetaData mData : metaDataList) {
			DatabaseDTO db = mData.cast();
			List<MetaData> childrens = db.getChilds();
			for (MetaData child : childrens) {
				TableDTO tableDto = child.cast();
				if (tableDto.getName().equals(table.name())) {
					return db;
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
		ServerRegistration registration = serverRegistor.getRegistration(auth, shardAge);
		List<RealServer> realServers = registration.getAllServers();
		AuthorityScheme authScheme = getAuthScheme(globalInfo.getAccessString());
		List<ServerScheme> schemes = Lists.newArrayList();
		for (RealServer rServer : realServers) {
			ServerScheme serverScheme = new ServerScheme(rServer.getHost(), rServer.getPort(), rServer.getProxyPort(),
					authScheme);
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

}
