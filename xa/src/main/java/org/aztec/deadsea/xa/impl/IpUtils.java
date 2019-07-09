package org.aztec.deadsea.xa.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {
	

	public IpUtils() {
		// TODO Auto-generated constructor stub
	}

	public static InetAddress getLocalAddress() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		return addr;
	}
	
	public static String getLocalHostName() throws UnknownHostException {
		InetAddress addr = getLocalAddress();
		return addr.getHostName();
	}
	
}
