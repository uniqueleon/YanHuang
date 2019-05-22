package org.aztec.deadsea.sql.impl.druid;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.aztec.autumn.common.utils.FileUtils;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;
import org.aztec.deadsea.sql.ConnectionConfiguration;
import org.aztec.deadsea.sql.DatabaseConnector;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSourceFactory;

@Component
@Scope("singleton")
public class DruidConnector implements DatabaseConnector{
	
	private DataSource ds = null;
	
	
	public DruidConnector() {
		// TODO Auto-generated constructor stub
	}

	public Connection connect(ConnectionConfiguration conf) throws ShardingSqlException {
		// TODO Auto-generated method stub
		
		try {
			if (ds != null) {
				return ds.getConnection();
			}
			Properties prop = new Properties();
			ByteArrayInputStream bis = new ByteArrayInputStream(conf.getText().getBytes("UTF-8"));
			prop.load(bis);
			String driverName = prop.getProperty("driverClassName","com.mysql.jdbc.Driver");
			driverName = driverName.trim();
			Class.forName(driverName);
			if(ds == null) {
				ds = DruidDataSourceFactory.createDataSource(prop);
			}
			return ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShardingSqlException(ErrorCodes.META_DATA_ERROR);
		}
	}
	
	public static void main(String[] args) {
		try {
			ConnectionConfiguration cc = new ConnectionConfiguration(FileUtils.readFileAsString(new File("conf/druid_connect.properties")));
			DruidConnector dc = new DruidConnector();
			Connection con = dc.connect(cc);
			Statement stmt = con.createStatement();
			System.out.println(stmt.executeQuery("select 1"));
			System.out.println(con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
