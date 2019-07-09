package org.aztec.deadsea.xa.impl;

import java.net.UnknownHostException;

import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAException.ErrorCodes;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.common.xa.XAResponseBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleXAResponseBuilder implements XAResponseBuilder {

	public SimpleXAResponseBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	private String getHost() throws UnknownHostException {
		return IpUtils.getLocalHostName();
	}
	
	private Integer getRunningPort() {
		return 0;
	}

	@Override
	public XAResponse buildSuccess(String tx, int assignmentNo, TransactionPhase phase) throws XAException {
		try {
			SimpleXAResponse xaResp = new SimpleXAResponse(getHost(), tx, phase, true, false, false);
			xaResp.setNo(assignmentNo);
			return xaResp;
		} catch (UnknownHostException e) {
			throw new XAException(ErrorCodes.GET_LOCAL_HOST_INFO_ERROR);
		}
	}

	@Override
	public XAResponse buildFail(String tx, int assignmentNo, Throwable t, TransactionPhase phase) throws XAException {
		try {
			SimpleXAResponse xaResp = new SimpleXAResponse(getHost(), tx, phase, false, false, false);
			xaResp.setNo(assignmentNo);
			return xaResp;
		} catch (UnknownHostException e) {
			throw new XAException(ErrorCodes.GET_LOCAL_HOST_INFO_ERROR);
		}
	}

	@Override
	public XAResponse buildFail(String tx, int assignmentNo, int errorCode, TransactionPhase phase) throws XAException {
		try {
			SimpleXAResponse xaResp = new SimpleXAResponse(getHost(), tx, phase, assignmentNo,errorCode);
			xaResp.setNo(assignmentNo);
			return xaResp;
		} catch (UnknownHostException e) {
			throw new XAException(ErrorCodes.GET_LOCAL_HOST_INFO_ERROR);
		}
	}

}
