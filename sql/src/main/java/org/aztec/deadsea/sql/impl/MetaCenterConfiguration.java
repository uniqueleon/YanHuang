package org.aztec.deadsea.sql.impl;

import java.util.List;
import java.util.Map;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.MetaDataRegister.MetaDataMapKeys;
import org.aztec.deadsea.common.entity.DatabaseDTO;
import org.aztec.deadsea.common.entity.TableDTO;
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
		List<MetaData> dbMetaDatas = metaDatas.get(MetaDataMapKeys.DATA_BASE_KEY);
		DatabaseDTO dbDTO = findMatchDatabase(dbMetaDatas, table);
		TableScheme ts = new TableScheme();
		return null;
	}
	
	private TableScheme wrapTableScheme() {
		
	}
	
	public DatabaseDTO findMatchDatabase(List<MetaData> metaDataList ,Table table) {
		for(MetaData mData : metaDataList) {
			DatabaseDTO db = mData.cast();
			List<MetaData> childrens = db.getChilds();
			for(MetaData child : childrens) {
				TableDTO tableDto = child.cast();
				if(tableDto.getName().equals(table.name())) {
					return db;
				}
			}
		}
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
