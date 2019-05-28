package org.aztec.deadsea.sql.impl.druid;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;
import org.aztec.deadsea.sql.ConnectionConfiguration;
import org.aztec.deadsea.sql.DatabaseConnector;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.common.collect.Maps;

@Component
@Scope("singleton")
public class DruidConnector implements DatabaseConnector{
	
	private Map<String,DataSource> pool = Maps.newConcurrentMap();
	
	public DruidConnector() {
		// TODO Auto-generated constructor stub
	}

	public Connection connect(ConnectionConfiguration conf) throws ShardingSqlException {
		// TODO Auto-generated method stub
		
		try {
			if (pool.containsKey(conf.getId())) {
				return pool.get(conf.getId()).getConnection();
			}
			Properties prop = new Properties();
			ByteArrayInputStream bis = new ByteArrayInputStream(conf.getText().getBytes("UTF-8"));
			prop.load(bis);
			String driverName = prop.getProperty("driverClassName","com.mysql.jdbc.Driver");
			driverName = driverName.trim();
			Class.forName(driverName);
			DataSource ds = DruidDataSourceFactory.createDataSource(prop);
			pool.put(conf.getId(), ds);
			return ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShardingSqlException(ErrorCodes.META_DATA_ERROR);
		}
	}

}
