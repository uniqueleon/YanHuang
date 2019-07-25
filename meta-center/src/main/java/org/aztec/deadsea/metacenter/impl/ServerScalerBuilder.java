package org.aztec.deadsea.metacenter.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.deadsea.common.ServerScaler;
import org.aztec.deadsea.common.impl.ModerateScaler;
import org.aztec.deadsea.metacenter.conf.zk.Account;
import org.aztec.deadsea.metacenter.conf.zk.BaseInfo;
import org.aztec.deadsea.metacenter.conf.zk.DatabaseInfo;
import org.aztec.deadsea.metacenter.conf.zk.RealServerInfo;
import org.aztec.deadsea.metacenter.conf.zk.ServerAge;
import org.aztec.deadsea.metacenter.conf.zk.TableInfo;
import org.aztec.deadsea.metacenter.utils.DatabaseUtils;

import com.google.common.collect.Lists;

public class ServerScalerBuilder {

	public ServerScalerBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	private boolean isAgeDataCleaned(ServerAge age) {
		return (age.getDataSize() != null && age.getNodeSize() != null && age.getDbSize() != null
				&& age.getTableSize() != null) && age.getClean();
	}

	public ServerScaler build(List<Account> accounts, ServerAge age) throws Exception {
		if(!isAgeDataCleaned(age)) {
			age.setDataSize(calculateDataSize(accounts, age.getServers()));
			age.setNodeSize(calculateNodeSize(age.getServers()));
			age.setDbSize(getDatabaseSize(accounts) );
			age.setTableSize(calcalateTableSize(accounts));
			age.setClean(true);
			age.save();
		}
		ModerateScaler retScaler = new ModerateScaler(age.getDataSize(),
				age.getTableSize(), age.getServers().size(), age.getDbSize(),age.getTableSize());
		return retScaler;
	}
	
	public Long calculateNodeSize(List<RealServerInfo> servers) {
		if(!CollectionUtils.isEmpty(servers) && 
				!CollectionUtils.isEmpty(servers.get(0).getNodes())) {
			return servers.get(0).getNodes().get(0).getModulus();
		}
		return 0l;
	}

	public long calcalateTableSize(List<Account> allAccounts) {
		long tableSize = 1l;
		for (Account account : allAccounts) {
			if (!CollectionUtils.isEmpty(account.getDatabases())) {
				for (DatabaseInfo db : account.getDatabases()) {
					if (CollectionUtils.isNotEmpty(db.getTables())) {
						for (TableInfo table : db.getTables()) {
							if (table.getShard()) {
								if (tableSize == 1) {
									tableSize = table.getSize();
								} else {
									GreatestCommonDivisor gcd = GreatestCommonDivisor.calculate(tableSize,
											table.getSize() * 1l);
									if (gcd.getGcd() == 1l) {
										tableSize *= table.getSize();
									} else {
										tableSize *= (table.getSize() / gcd.getGcd());
									}
								}
							}
						}
					}
				}
			}
		}
		return tableSize;
	}

	private long getDatabaseSize(List<Account> allAccounts) {
		long databaseSize = 1l;
		for (Account account : allAccounts) {
			if (!CollectionUtils.isEmpty(account.getDatabases())) {
				for (DatabaseInfo db : account.getDatabases()) {
					if (databaseSize == 1) {
						databaseSize = db.getSize();
					} else {
						GreatestCommonDivisor gcd = GreatestCommonDivisor.calculate(databaseSize, db.getSize() * 1l);
						if (gcd.getGcd() == 1l) {
							databaseSize *= db.getSize();
						} else {
							databaseSize *= (db.getSize() / gcd.getGcd());
						}
					}
				}
			}
		}
		return databaseSize;
	}

	private List<String> getTablePrefixes(List<Account> allAccounts) {
		List<String> tableNames = Lists.newArrayList();
		for (Account account : allAccounts) {
			if (!CollectionUtils.isEmpty(account.getDatabases())) {
				for (DatabaseInfo db : account.getDatabases()) {
					if (!CollectionUtils.isNotEmpty(db.getTables())) {
						for (TableInfo table : db.getTables()) {
							if (table.getShard()) {
								tableNames.add(table.getName());
							}
						}
					}
				}
			}
		}
		return tableNames;
	}

	private String[][] getConnectArgs(List<RealServerInfo> realServers) {
		String[][] args = new String[realServers.size()][];
		BaseInfo baseInfo = BaseInfo.getInstance();
		for (int i = 0; i < realServers.size(); i++) {
			args[i] = new String[3];
			RealServerInfo rs = realServers.get(i);
			String url = "jdbc:mysql://" + rs.getHost() + ":" + rs.getPort() + "/";
			args[i][0] = url;
			String[] usrPwd = baseInfo.getGlobalAccessString().split(":");
			args[i][1] = usrPwd[0];
			args[i][2] = usrPwd[1];
		}
		return args;
	}

	public long calculateDataSize(List<Account> accounts, List<RealServerInfo> realServers)
			throws ClassNotFoundException, SQLException {

		return DatabaseUtils.countTotalDataSize(getConnectArgs(realServers), getTablePrefixes(accounts));
	}
}
