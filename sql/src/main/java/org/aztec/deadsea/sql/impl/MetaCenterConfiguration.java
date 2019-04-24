package org.aztec.deadsea.sql.impl;

import java.util.List;
import java.util.Map;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.conf.LocalAuthConfiguration;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.conf.TableScheme;
import org.aztec.deadsea.sql.meta.Table;
import org.springframework.beans.factory.annotation.Autowired;

public class MetaCenterConfiguration implements ShardingConfiguration {
	
	@Autowired
	private MetaDataRegister registor;

	private LocalAuthConfiguration localConf;
	
	private Authentication auth;
	

	public MetaCenterConfiguration() throws DeadSeaException {
		// TODO Auto-generated constructor stub
		localConf = new LocalAuthConfiguration();
		auth = registor.auth(localConf.getUsername(), localConf.getPassword());
	}

	@Override
	public TableScheme getTargetTable(Table table) throws DeadSeaException {
		Map<String,List<MetaData>> metaDatas = registor.getRegistedMetaDatas(auth);
		return null;
	}

	@Override
	public List<ServerScheme> getRealServers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServerScheme> getVirtualServers() {
		// TODO Auto-generated method stub
		return null;
	}

}
