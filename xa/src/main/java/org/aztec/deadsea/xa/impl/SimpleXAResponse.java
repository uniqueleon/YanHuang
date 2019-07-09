package org.aztec.deadsea.xa.impl;

import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAResponse;

public class SimpleXAResponse implements XAResponse {
	
	private String host;
	private String id;
	private TransactionPhase phase;
	private boolean ok = false;
	private boolean fail = false;
	private boolean missed = false;
	private Integer no;
	private Integer errorCode;

	public SimpleXAResponse(String host, String id, TransactionPhase phase, boolean ok, boolean fail, boolean missed
			) {
		super();
		this.host = host;
		this.id = id;
		this.phase = phase;
		this.ok = ok;
		this.fail = fail;
		this.missed = missed;
	}

	public SimpleXAResponse(String host, String id, TransactionPhase phase, Integer no, Integer errorCode) {
		super();
		this.host = host;
		this.id = id;
		this.phase = phase;
		this.no = no;
		this.errorCode = errorCode;
		ok = false;
		fail = true;
		missed = false;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TransactionPhase getPhase() {
		return phase;
	}

	public void setPhase(TransactionPhase phase) {
		this.phase = phase;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setFail(boolean fail) {
		this.fail = fail;
	}

	public void setMissed(boolean missed) {
		this.missed = missed;
	}


	public TransactionPhase getCurrentPhase() {
		return phase;
	}

	public String getId() {
		return id;
	}

	public String getHost() {
		return host;
	}

	public boolean isFail() {
		return fail;
	}

	public boolean isMissed() {
		return missed;
	}

	@Override
	public int getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	
}
