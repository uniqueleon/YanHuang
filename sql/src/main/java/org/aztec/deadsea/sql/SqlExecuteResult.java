package org.aztec.deadsea.sql;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface SqlExecuteResult {

	public ResultSet getResultSet();
	public int getAffectRow();
	public boolean isSuccess();
	public int getSuccessCount();
	public Map<String,Object> getTempData();
	public int getErrorCount();
	public List<String> getErrorMessages();
}
