package org.aztec.deadsea.sql;

import java.util.List;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.conf.TableScheme;
import org.aztec.deadsea.sql.meta.Table;

public interface ShardingConfiguration {

	public TableScheme getTargetTable(Table table) throws DeadSeaException;
	public List<ServerScheme> getRealServers(Integer age) throws DeadSeaException;
	public List<List<ServerScheme>> getAllRealServers() throws DeadSeaException;
	public ShardingAge getCurrentAge() throws DeadSeaException;
	public Authentication getAuth() throws DeadSeaException;
}
