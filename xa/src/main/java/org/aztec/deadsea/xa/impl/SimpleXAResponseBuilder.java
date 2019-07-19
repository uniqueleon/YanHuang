package org.aztec.deadsea.xa.impl;

import java.net.UnknownHostException;
import java.util.Map;

import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.common.xa.XAResponseBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleXAResponseBuilder implements XAResponseBuilder {

	public SimpleXAResponseBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	private String getHost()  {
		try {
			return IpUtils.getLocalHostName();
		} catch (UnknownHostException e) {
			return "";
		}
	}
	

	@Override
	public XAResponse buildSuccess(String tx, int assignmentNo, TransactionPhase phase) {
		SimpleXAResponse xaResp = new SimpleXAResponse(getHost(), tx, phase, true, false, false);
		xaResp.setNo(assignmentNo);
		return xaResp;
	}

	@Override
	public XAResponse buildFail(String tx, int assignmentNo, Throwable t, TransactionPhase phase) {
		SimpleXAResponse xaResp = new SimpleXAResponse(getHost(), tx, phase, false, true, false);
		xaResp.setNo(assignmentNo);
		return xaResp;
	}

	@Override
	public XAResponse buildFail(String tx, int assignmentNo, int errorCode, TransactionPhase phase) {
		SimpleXAResponse xaResp = new SimpleXAResponse(getHost(), tx, phase, assignmentNo,errorCode);
		xaResp.setNo(assignmentNo);
		return xaResp;
	}

	@Override
	public XAResponse buildFromMap(String tx, Map<String, Object> contents, TransactionPhase phase) {
		SimpleXAResponse xaResp = new SimpleXAResponse(getHost(), tx, phase,(boolean)contents.get("ok"),(boolean)contents.get("fail"),(boolean)contents.get("missed"));
		xaResp.setNo((int)contents.get("no"));
		return xaResp;
	}

}
