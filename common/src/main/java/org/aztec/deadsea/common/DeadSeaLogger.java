package org.aztec.deadsea.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeadSeaLogger {
	
	public static final Logger logger = LoggerFactory.getLogger("DEADSEA");
	

	public DeadSeaLogger() {
		// TODO Auto-generated constructor stub
	}
	
	public static void info(String prefix,String msg) {
		logger.info(getLogPrefix(prefix) + msg);
	}
	
	public static void error(String prefix,Throwable e) {
		//logger.error(printStackTrace(prefix, e));
		logger.error(prefix + e.getMessage(),e);
	}
	
	public static void warn(String prefix,String msg) {
		logger.warn(getLogPrefix(prefix) + msg);
	}
	
	public static void warn(String prefix, Throwable a) {
		logger.warn(printStackTrace(prefix, a));
	}
	
	private static String printStackTrace(String prefix,Throwable e) {
		StringBuilder builder = new StringBuilder(getLogPrefix(prefix) + e.getMessage());
		for(StackTraceElement ste : e.getStackTrace()) {
			builder.append(ste.getClassName() + "#" + ste.getMethodName() + ":" + ste.getLineNumber());
		}
		return builder.toString();
		
	}
	
	private static String getLogPrefix(String prefix) {
		return "[" + prefix + "]:";
	}

}
