package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.metacenter.MetaCenterConst;
import org.aztec.deadsea.metacenter.MetaCenterLogger;
import org.aztec.deadsea.metacenter.MetaDataException;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;

import com.google.common.collect.Lists;

public class DatabaseInfo extends ZkConfig{

	//数据库名称前缀  XXX_001
	private String name;
	//分库数
	private Integer size;
	
	private Integer tableNum;
	@Ignored
	private List<TableInfo> tables;
	
	public DatabaseInfo(String uuid) throws IOException, KeeperException, InterruptedException {
		// TODO Auto-generated constructor stub
		super(MetaCenterConst.ZkConfigPaths.BASE_INFO, ConfigFormat.JSON);
	}
	
	public List<TableInfo> getTables() {
		return tables;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getTableNum() {
		return tableNum;
	}

	public void setTableNum(Integer tableNum) {
		this.tableNum = tableNum;
	}

	public void setTables(List<TableInfo> tables) {
		this.tables = tables;
	}



	private class TableReloader implements TimeLimitedCallable {

		public Object call() throws Exception {
			if(!CollectionUtils.isEmpty(tables)) {
				for(TableInfo table : tables) {
					table.destroy();
				}
				tables.clear();
			}
			loadTables();
			return null;
		}
		
		public void loadTables() throws IOException, KeeperException, InterruptedException {
			
			tables = Lists.newArrayList();
			if(tableNum == null) {
				tableNum = 0;
			}
			for(int i = 0;i < tableNum ;i++) {
				tables.add(new TableInfo(i));
			}
		}

		public Long getTime() {
			BaseInfo baseInfo = BaseInfo.getInstance();
			return baseInfo.getLoadTableTimeout();
		}

		public TimeUnit getUnit() {
			return TimeUnit.MILLISECONDS;
		}

		public void interupt() {
			// TODO Auto-generated method stub
			
		}
		
	}
	

	public void registTable(TableInfo table) throws MetaDataException {
		try {
			if(tables.size() > table.getNo()) {
				throw new MetaDataException(ErrorCodes.META_DATA_INFO_CONFLICT);
			}
			else {
				tables.add(table);
			}
			table.save();
		} catch (Exception e) {
			MetaCenterLogger.error(e);
			throw new MetaDataException(2);
		}
	}
}
