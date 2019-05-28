package org.aztec.deadsea.sql;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface SqlExecuteResult {

	public ResultSet getResultSet();
	public void appendResult(ResultSet result);
	public int getAffectRow();
	public void fail();
	public void success();
	public void finish(int exectCount,boolean success);
	public boolean isSuccess();
	public int getSuccessCount();
	public Map<String,Object> getTempData();
	public int getErrorCount();
	public List<String> getErrorMessages();
	public void merge(SqlExecuteResult result);
}
