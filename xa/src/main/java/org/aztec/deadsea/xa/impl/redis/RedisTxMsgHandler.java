package org.aztec.deadsea.xa.impl.redis;

import java.util.Map;
import java.util.concurrent.Callable;

import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAProposal;

public interface RedisTxMsgHandler{

	public boolean accept(String channel,XAProposal proposal);
	public boolean isAssignable();
	public void handle(XAProposal proposal,TransactionPhase phase,XAContext context,
			Map<String,Object> datas);
	
}
