package org.aztec.deadsea.sql.impl;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.aztec.deadsea.sql.SqlExecuteResult;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

public class BaseSqlExecResult implements SqlExecuteResult {
	
	protected List<String> errMsgs = Lists.newArrayList();
	protected boolean success = false;
	protected int errorCount = 0;
	protected int affectRow = 0;
	protected Map<String,Object> tmpDatas;

	public BaseSqlExecResult(boolean success) {

		this.success = success;
		tmpDatas = Maps.newHashMap();
	}

	@Override
	public ResultSet getResultSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAffectRow() {
		return affectRow;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}

	@Override
	public int getErrorCount() {
		return errorCount;
	}

	@Override
	public List<String> getErrorMessages() {
		return errMsgs;
	}

	@Override
	public int getSuccessCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Object> getTempData() {
		// TODO Auto-generated method stub
		return null;
	}

}
