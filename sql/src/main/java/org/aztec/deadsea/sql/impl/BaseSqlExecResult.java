package org.aztec.deadsea.sql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.aztec.deadsea.common.DeadSeaLogger;
import org.aztec.deadsea.sql.SqlExecuteResult;
import org.aztec.deadsea.sql.SqlModularConstant;
import org.aztec.deadsea.sql.impl.result.MultipleResultSet;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

public class BaseSqlExecResult implements SqlExecuteResult {
	
	protected List<String> errMsgs = Lists.newArrayList();
	protected boolean success = false;
	protected int errorCount = 0;
	protected int affectRow = 0;
	protected int successCount = 0;
	protected int totalCount = 0;
	protected Map<String,Object> tmpDatas;
	protected MultipleResultSet resultSet;

	public BaseSqlExecResult(boolean success) {

		this.success = success;
		tmpDatas = Maps.newHashMap();
		resultSet = new MultipleResultSet();
	}

	@Override
	public ResultSet getResultSet() {
		return resultSet;
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
		return successCount;
	}

	@Override
	public Map<String, Object> getTempData() {
		return tmpDatas;
	}

	public List<String> getErrMsgs() {
		return errMsgs;
	}

	public void setErrMsgs(List<String> errMsgs) {
		this.errMsgs = errMsgs;
	}

	public Map<String, Object> getTmpDatas() {
		return tmpDatas;
	}

	public void setTmpDatas(Map<String, Object> tmpDatas) {
		this.tmpDatas = tmpDatas;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public void setAffectRow(int affectRow) {
		this.affectRow = affectRow;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}


	@Override
	public void appendResult(ResultSet result) {
		resultSet.getResultList().add(result);
		try {
			result.beforeFirst();
			while(result.next()) {
				totalCount ++;
			}
			result.beforeFirst();
		} catch (SQLException e) {
			DeadSeaLogger.error(SqlModularConstant.LOG_PREFIX, e);
		}
	}

	@Override
	public void success() {
		successCount += 1;
		success = true;
	}

	@Override
	public void fail() {
		errorCount += 1;
		success = false;
	}

	@Override
	public void finish(int count, boolean success) {
		this.totalCount = count;
		this.success = success;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setResultSet(MultipleResultSet resultSet) {
		this.resultSet = resultSet;
	}

	@Override
	public void merge(SqlExecuteResult result) {
		if(result instanceof BaseSqlExecResult) {
			BaseSqlExecResult baseRes = (BaseSqlExecResult) result;
			this.affectRow += baseRes.affectRow;
			this.errMsgs.addAll(baseRes.errMsgs);
			this.errorCount += baseRes.errorCount;
			this.successCount += baseRes.successCount;
			this.totalCount += baseRes.totalCount;
			this.success &= baseRes.success;
			resultSet.merge(baseRes.resultSet);
		}
	}

}
