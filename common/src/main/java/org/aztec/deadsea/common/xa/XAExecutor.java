package org.aztec.deadsea.common.xa;

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
	public XAContext prepare(XAProposal request,XAContext context);
	/**
	 * 提交
	 * @param context
	 * @return
	 */
	public XAContext commit(XAContext context);
	/**
	 * 回滚
	 * @param context
	 * @return
	 */
	public XAContext rollback(XAContext context);
}
