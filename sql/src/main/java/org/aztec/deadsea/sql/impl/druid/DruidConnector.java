package org.aztec.deadsea.sql.impl.druid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.aztec.deadsea.sql.ConnectionConfiguration;
import org.aztec.deadsea.sql.DatabaseConnector;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidConnector implements DatabaseConnector{
	
	public DruidConnector() {
		// TODO Auto-generated constructor stub
	}

	public Connection connect(ConnectionConfiguration conf) throws SQLException, IOException {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		ByteArrayInputStream bis = new ByteArrayInputStream(conf.getText().getBytes("UTF-8"));
		prop.load(bis);
		DruidDataSource ds = new DruidDataSource();
		ds.configFromPropety(prop);
		return ds.getConnection();
	}

}
