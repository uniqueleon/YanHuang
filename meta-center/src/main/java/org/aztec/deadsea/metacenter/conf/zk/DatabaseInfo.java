package org.aztec.deadsea.metacenter.conf.zk;

import java.util.List;

import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.zk.AbstractSubNodeReloader;
import org.aztec.autumn.common.zk.CallableWatcher;
import org.aztec.autumn.common.zk.Ignored;
import org.aztec.autumn.common.zk.TimeLimitedCallable;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.deadsea.common.MetaData;
import org.aztec.deadsea.common.entity.DatabaseDTO;
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
	@Ignored
	private Integer tableNum;
	
	private boolean shard;
	@Ignored
	private List<TableInfo> tables;
	@Ignored
	private List<TimeLimitedCallable> callbacks; 
	
	public DatabaseInfo(String prefix,int no) throws Exception {
		// TODO Auto-generated constructor stub
		super(prefix + GlobalConst.ZOOKEEPER_PATH_SPLITOR + no, ConfigFormat.JSON);
		if(!isDeprecated) {
			initDb();
		}
	}
	
	private void initDb() throws Exception {
		callbacks = Lists.newArrayList();
		TableReloader reloader = new TableReloader(this);
		callbacks.add(reloader);
		reloader.load();
		appendWatcher(new CallableWatcher(callbacks, null));
	}
	
	public void refresh() throws Exception {
		TableReloader loader = (TableReloader) callbacks.get(0);
		loader.load();
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

	private class TableReloader extends AbstractSubNodeReloader {

		public TableReloader(ZkConfig parent) {
			super(parent);
			// TODO Auto-generated constructor stub
		}


		public Long getTime() {
			BaseInfo baseInfo = BaseInfo.getInstance();
			return baseInfo.getLoadTableTimeout();
		}


		@Override
		protected ZkConfig loadChild(int index) throws Exception {
			return new TableInfo(znode,index);
		}
		
	}
	
	public MetaData toMetaData() {
		DatabaseDTO db = new DatabaseDTO(no, name, size, tableNum, shard,Lists.newArrayList());
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

	@Override
	public void destroy() {
		super.destroy();
		for(TableInfo table : tables) {
			table.destroy();
		}
	}
	
	
}
