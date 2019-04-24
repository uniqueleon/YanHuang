package org.aztec.deadsea.metacenter.conf.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.zk.CallableWatcher;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.entity.Database;
import org.aztec.deadsea.metacenter.MetaCenterLogger;
import org.aztec.deadsea.metacenter.MetaDataException;
import org.aztec.deadsea.metacenter.MetaDataException.ErrorCodes;

import com.google.common.collect.Lists;

public class DatabaseInfo extends ZkConfig{

	private int no;
	//数据库名称前缀  XXX_001
	private String name;
	
	//分库数
	private Integer size;
	
	private Integer tableNum;
	
	private boolean shard;
	@Ignored
	private List<TableInfo> tables;
	@Ignored
	private List<TimeLimitedCallable> callbacks; 
	
	public DatabaseInfo(String prefix,int no) throws IOException, KeeperException, InterruptedException {
		// TODO Auto-generated constructor stub
		super(prefix + GlobalConst.ZOOKEEPER_PATH_SPLITOR + no, ConfigFormat.JSON);
		initDb();
	}
	
	public DatabaseInfo(String path) throws IOException, KeeperException, InterruptedException {
		// TODO Auto-generated constructor stub
		super(path, ConfigFormat.JSON);
		initDb();
	}
	
	private void initDb() {

		callbacks.add(new TableReloader());
		appendWatcher(new CallableWatcher(callbacks, null));
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

	public boolean isShard() {
		return shard;
	}

	public void setShard(boolean shard) {
		this.shard = shard;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
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
				tables.add(new TableInfo(znode,i));
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
	
	public MetaData toMetaData() {
		Database db = new Database(no, name, size, tableNum, shard,Lists.newArrayList());
		for(TableInfo t : tables) {
			db.getChilds().add(t.toMetaData(db));
		}
		return db;
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
