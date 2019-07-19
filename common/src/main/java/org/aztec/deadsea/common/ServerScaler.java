package org.aztec.deadsea.common;

import java.util.List;

/**
 * 服务器平滑扩容接口,根据目前服务器的数量负载情况自动平滑扩容,
 * @author 10064513
 *
 */
public interface ServerScaler {

	/**
	 * 获取真实的服务器数量
	 * @return
	 * @throws DeadSeaException
	 */
	public long getActualSize() throws DeadSeaException;
	/**
	 * 获取下次预期的服务器数量
	 * @return
	 * @throws DeadSeaException
	 */
	public long getNextActualSize() throws DeadSeaException;
	/**
	 * 获取当前虚拟结点数量
	 * @return
	 * @throws DeadSeaException
	 */
	public long getCurrentSize() throws DeadSeaException;
	/**
	 * 获取下次预测的虚拟结点数量
	 * @return
	 * @throws DeadSeaException
	 */
	public long getNextSize() throws DeadSeaException;
	/**
	 * 获取负载均衡阀值
	 * @return
	 * @throws DeadSeaException
	 */
	public long getBalanceValve() throws DeadSeaException;
	/**
	 * 当达到均衡阀值时，重新分配负载
	 * @param servers
	 * @return
	 * @throws DeadSeaException
	 */
	public List<RealServer> rebalance(List<RealServer> servers) throws DeadSeaException;
	/**
	 * 当增加结点时，重新分配负载
	 * @param servers
	 * @param force
	 * @return
	 * @throws DeadSeaException
	 */
	public List<RealServer> reallocate4Inc(List<RealServer> servers,boolean force) throws DeadSeaException;
	/**
	 * 当减少结点时，重新分配负载
	 * @param servers
	 * @param force
	 * @return
	 * @throws DeadSeaException
	 */
	public List<RealServer> reallocate4Dec(List<RealServer> servers,boolean force) throws DeadSeaException;
}
