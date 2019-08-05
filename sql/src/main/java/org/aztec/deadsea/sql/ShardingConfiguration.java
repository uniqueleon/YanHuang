package org.aztec.deadsea.sql;

import java.util.List;

import org.aztec.deadsea.common.Authentication;
import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.common.entity.DatabaseDTO;
import org.aztec.deadsea.common.entity.GlobalInfoDTO;
import org.aztec.deadsea.common.entity.TableDTO;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.meta.Database;
import org.aztec.deadsea.sql.meta.Table;

public interface ShardingConfiguration {

	public TableDTO getTargetTable(Table table) throws DeadSeaException;
	public DatabaseDTO getDatabaseScheme(Database database) throws DeadSeaException;
	public List<ServerScheme> getRealServers(Integer age) throws DeadSeaException;
	public List<List<ServerScheme>> getAllRealServers() throws DeadSeaException;
	public ShardingAge getCurrentAge() throws DeadSeaException;
	public Authentication getAuth() throws DeadSeaException;
	public GlobalInfoDTO getGlobal() throws DeadSeaException;
}
