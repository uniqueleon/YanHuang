package org.aztec.deadsea.sql;

/**
 * 切片sql执行器
 * @author 10064513
 *
 */
public interface ShardSqlExecutor {

	public static enum ExecuteMode{
		//逐条sql发送到数据库,防止一次从网络加载太多数据,主要用于查询
		SINGLE,
		//批量sql发送到数据库，提高sql执行效率
		BATCH;
	}
	/**
	 * 执行sql(DDL),默认为batch
	 * @param sql
	 * @param execType
	 * @return
	 * @throws ShardingSqlException
	 */
	public SqlExecuteResult execute(String sql,ExecuteMode execType) throws ShardingSqlException;
	/**
	 * 执行查询,type可不填，默认为single
	 */
	public SqlExecuteResult executeQuery(String sql,ExecuteMode execType) throws ShardingSqlException;
	/**
	 * 执行更新,type可不填，默认为batch
	 * @param sql
	 * @param execType
	 * @return
	 * @throws ShardingSqlException
	 */
	public SqlExecuteResult executeUpdate(String sql,ExecuteMode execType) throws ShardingSqlException;
	
}
