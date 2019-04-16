package org.aztec.deadsea.metacenter;

import org.slf4j.Logger;

public class MetaCenterLogger {
	
	private final static Logger LOG = org.slf4j.LoggerFactory.getLogger(MetaCenterLogger.class);

	private MetaCenterLogger() {
		// TODO Auto-generated constructor stub
	}
	
	public static Logger getLogger() {
		return LOG;
	}
	
	public static void error(Throwable t) {
		LOG.error(t.getMessage(),t);
	}
	
	public static void error(String msg) {
		LOG.error(msg);
	}
	
	public static void info(String msg) {
		LOG.info(msg);
	}
	
	public static void debug(String msg) {
		LOG.debug(msg);
	}
	public static void warn(String msg) {
		LOG.warn(msg);
	}

}
