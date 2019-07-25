package org.aztec.deadsea.sql.impl.executor;

import java.util.List;

import org.aztec.deadsea.common.DeadSeaException;
import org.aztec.deadsea.common.RoutingInfoBuilder;
import org.aztec.deadsea.common.ShardData;
import org.aztec.deadsea.common.ShardingAge;
import org.aztec.deadsea.common.VirtualServer;
import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.impl.executor.BaseSqlExecutor.ExecuteType;

import com.google.common.collect.Lists;

public class XASqlExecuteParameter {
	
	private List<ServerScheme> schemes;
	private List<List<SQLPair>> pairs;
	private ExecuteType type;

	public XASqlExecuteParameter() {
		// TODO Auto-generated constructor stub
	}

	public XASqlExecuteParameter(List<String> sqls, List<String> rollbacks
			,ExecuteType type,ShardingConfiguration conf,RoutingInfoBuilder routeBuilder,GenerationParameter gp) throws DeadSeaException {
		super();
		this.type = type;
		init(SQLPair.toPairs(sqls, rollbacks), conf, routeBuilder, gp);
	}

	public List<ServerScheme> getSchemes() {
		return schemes;
	}

	public void setSchemes(List<ServerScheme> schemes) {
		this.schemes = schemes;
	}

	
	public static class SQLPair{
		String sql;
		String rollback;
		public String getSql() {
			return sql;
		}
		public void setSql(String sql) {
			this.sql = sql;
		}
		public String getRollback() {
			return rollback;
		}
		public void setRollback(String rollback) {
			this.rollback = rollback;
		}
		public SQLPair(String sql, String rollback) {
			super();
			this.sql = sql;
			this.rollback = rollback;
		}
		public static List<SQLPair> toPairs(List<String> sqls,List<String> rollbacks){
			List<SQLPair> pairs = Lists.newArrayList();
			for(int i = 0;i < sqls.size();i++) {
				pairs.add(new SQLPair(sqls.get(i), rollbacks.size() > i ? rollbacks.get(i) : ""));
			}
			return pairs;
		}
	}

	public void init(List<SQLPair> tmpPairs,ShardingConfiguration conf,RoutingInfoBuilder routeBuilder,GenerationParameter gp) throws DeadSeaException {
		ShardingAge age = conf.getCurrentAge();
		List<ServerScheme> servers = conf.getRealServers(age.getNo());
		schemes = Lists.newArrayList();
		pairs = Lists.newArrayList();
		for(ServerScheme server : servers) {
			for(VirtualServer vs : server.getNodes()) {
				List<Long> targetIDs = gp.getSqlMetaData().getTargetIds();
				if(targetIDs != null) {
					List<ShardData> sDatas = 
							vs.route(routeBuilder.build(Lists.newArrayList(targetIDs),tmpPairs));
					List<SQLPair> newPairs = Lists.newArrayList();
					if(sDatas.size() > 0) {
						schemes.add(server);
						for(ShardData sd : sDatas) {
							newPairs.add(sd.getData());
						}
					}
					pairs.add(newPairs);
				}
				else {
					schemes.add(server);
					List<SQLPair> newPairs = Lists.newArrayList();
					newPairs.addAll(tmpPairs);
					pairs.add(newPairs);
				}
			}
		}
	}

	public List<List<SQLPair>> getPairs() {
		return pairs;
	}

	public void setPairs(List<List<SQLPair>> pairs) {
		this.pairs = pairs;
	}
	
	
}
