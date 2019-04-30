package org.aztec.deadsea.common;

import java.util.List;

/**
 * 服务器平滑扩容接口,根据目前服务器的数量负载情况自动平滑扩容,
 * @author 10064513
 *
 */
public interface ServerScaler {

	/**
	 * 
	 * @return
	 * @throws DeadSeaException
	 */
	public long getActualSize() throws DeadSeaException;
	public long getNextActualSize() throws DeadSeaException;
	public long getCurrentSize() throws DeadSeaException;
	public long getNextSize() throws DeadSeaException;
	public long getBalanceValve() throws DeadSeaException;
	public List<RealServer> reallocate4Inc(List<RealServer> servers,boolean force) throws DeadSeaException;
	public List<RealServer> reallocate4Dec(List<RealServer> servers,boolean force) throws DeadSeaException;
}
