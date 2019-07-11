package org.aztec.deadsea.xa.impl;

import java.util.Map;
import java.util.UUID;

import org.aztec.deadsea.common.xa.DistributedTransactionManager;
import org.aztec.deadsea.common.xa.ProposalFactory;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XACoordinator;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAProposal;
import org.aztec.deadsea.common.xa.XAResponseSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
//@Scope("prototype")
public class XAHelper implements DistributedTransactionManager{
	
	@Autowired
	private XACoordinator coordinator;
	@Autowired
	private ProposalFactory factory;
	
	private static final Map<String,XARecord> records = Maps.newConcurrentMap();

	public XAHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public <T> T submit(int quorum,Map<String,Object> attachments,TransactionResultBuilder<T> builder) throws XAException{
		String txId = UUID.randomUUID().toString();
		XAProposal proposal = factory.createProposal(txId, XAConstant.XA_PROPOSAL_TYPES.XA_SQL,quorum, attachments);
		coordinator.prepare(proposal, this);
		XARecord<T> record = new XARecord<T>(proposal,null,builder);
		records.put(txId, record);
		synchronized (record) {
			try {
				record.wait();
			} catch (InterruptedException e) {
				throw new XAException(e, -1);
			}
		}
		return record.getExecResult();
		//return null;
	}


	private static class XARecord<T>{
		private XAProposal proposal;
		private T execResult;
		private TransactionResultBuilder<T> resultBuilder;
		
		public T getExecResult() {
			return execResult;
		}
		public void setExecResult(T execResult) {
			this.execResult = execResult;
		}
		public XAProposal getProposal() {
			return proposal;
		}
		public TransactionResultBuilder getResultBuilder() {
			return resultBuilder;
		}
		public XARecord(XAProposal proposal, XAResponseSet response,TransactionResultBuilder<T> builder) {
			super();
			this.proposal = proposal;
			this.resultBuilder = builder;
		}
	}
	


	@Override
	public void listen(XAResponseSet responses) throws XAException {
		System.out.println("receive " + responses.getCurrentPhase() + " MSG!");
		XARecord record = records.get(responses.getTxID());
		Object result;
		if(responses.isPassed()) {
			switch(responses.getCurrentPhase()) {
			case PREPARE:
				coordinator.commit(record.getProposal());
				break;
			case COMMIT:
				result = record.getResultBuilder().buildCommit(responses);
				record.setExecResult(result);
				synchronized (record) {
					record.notifyAll();
				}
				break;
			case ROLLBACK:
				coordinator.rollback(record.getProposal());
				result = record.getResultBuilder().buildRollBack(responses);
				record.setExecResult(result);
				synchronized (record) {
					record.notifyAll();
				}
			}
		}
		else {
			coordinator.rollback(record.getProposal());
			result = record.getResultBuilder().buildRollBack(responses);
			synchronized (record) {
				record.notifyAll();
			}
		}
	}
}
