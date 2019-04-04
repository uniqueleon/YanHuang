package org.aztec.deadsea.sql;

import java.util.List;

import org.aztec.deadsea.sql.meta.Table;
import org.aztec.deadsea.sql.scheme.ServerScheme;
import org.aztec.deadsea.sql.scheme.TableScheme;

public interface ShardingConfiguration {

	public TableScheme getTargetTable(Table table);
	public List<ServerScheme> getRealServers();
	public List<ServerScheme> getVirtualServers();
}
