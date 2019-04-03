package org.aztec.deadsea.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnector {

	public Connection connect(ConnectionConfiguration conf) throws SQLException,IOException ;
	
}
