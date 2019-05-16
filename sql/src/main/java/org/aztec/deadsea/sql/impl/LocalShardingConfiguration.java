package org.aztec.deadsea.sql.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.conf.DatabaseScheme;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.conf.TableScheme;
import org.aztec.deadsea.sql.meta.Table;

public class LocalShardingConfiguration implements ShardingConfiguration {
	
	
	private int tableNum = 10;

	public LocalShardingConfiguration() throws FileNotFoundException, IOException {

		File localConfigFile = new File("conf/db_scheme.properties");
		Properties prop = new Properties();
		if(localConfigFile.exists()) {
			prop.load(new FileInputStream(localConfigFile));
		}
	}

	public TableScheme getTargetTable(Table table) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ServerScheme> getRealServers() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ServerScheme> getVirtualServers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServerScheme> getRealServers(Integer age) throws DeadSeaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<ServerScheme>> getAllRealServers() throws DeadSeaException {
		// TODO Auto-generated method stub
		return null;
	}


}
