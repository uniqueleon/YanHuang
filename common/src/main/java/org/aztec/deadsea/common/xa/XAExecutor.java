package org.aztec.deadsea.common.xa;

/**
 * 分布式事务执行器
 * @author 10064513
 *
 */
public interface XAExecutor {

	/**
	 * 是否可以处理当
	 * @param context
	 * @return
	 * @throws XAException
	 */
	public boolean canHandle(XAContext context) throws XAException;
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
