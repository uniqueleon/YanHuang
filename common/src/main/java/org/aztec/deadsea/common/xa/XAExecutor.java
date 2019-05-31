package org.aztec.deadsea.common.xa;

import java.util.concurrent.Callable;

/**
 * 分布式事务执行器
 * @author 10064513
 *
 */
public interface XAExecutor {

	/**
	 * 准备
	 * @param request
	 * @param context
	 * @return
	 */
	public XAResponse prepare(XAContext context)  throws XAException;
	/**
	 * 提交
	 * @param context
	 * @return
	 */
	public XAResponse commit(XAContext context)  throws XAException;
	/**
	 * 回滚
	 * @param context
	 * @return
	 */
	public XAResponse rollback(XAContext context)  throws XAException;
}
