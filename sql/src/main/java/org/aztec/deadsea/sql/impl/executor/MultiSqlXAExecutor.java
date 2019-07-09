package org.aztec.deadsea.sql.impl.executor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.aztec.deadsea.common.xa.ProposalFactory;
import org.aztec.deadsea.common.xa.XACoordinator;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAPhaseListener;
import org.aztec.deadsea.common.xa.XAProposal;
import org.aztec.deadsea.common.xa.XAResponseSet;
import org.aztec.deadsea.sql.SqlExecuteResult;
import org.aztec.deadsea.sql.conf.ServerScheme;
import org.aztec.deadsea.sql.impl.BaseSqlExecResult;
import org.aztec.deadsea.sql.impl.xa.XAConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class MultiSqlXAExecutor extends BaseSqlExecutor implements XAPhaseListener{
	
	@Autowired
	private XACoordinator coordinator;
	@Autowired
	private ProposalFactory factory;
	private static final Map<String,XARecord> records = Maps.newConcurrentMap();
	
	
	private static class XARecord{
		private XAProposal proposal;
		private XAResponseSet response;
		private SqlExecuteResult execResult;
		
		public SqlExecuteResult getExecResult() {
			return execResult;
		}
		public void setExecResult(SqlExecuteResult execResult) {
			this.execResult = execResult;
		}
		public XAProposal getProposal() {
			return proposal;
		}
		public void setProposal(XAProposal proposal) {
			this.proposal = proposal;
		}
		public XAResponseSet getResponse() {
			return response;
		}
		public void setResponse(XAResponseSet response) {
			this.response = response;
		}
		public XARecord(XAProposal proposal, XAResponseSet response) {
			super();
			this.proposal = proposal;
			this.response = response;
		}
		
		
	}

	public MultiSqlXAExecutor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SqlExecuteResult doExecute(List<String> sqls,List<String> rollbacks, List<ServerScheme> schemes, ExecuteType type) throws Exception {
		
		int quorum = schemes.size();
		Map<String,Object> attachments = Maps.newHashMap();
		attachments.put(XAConstant.CONTEXT_KEYS.CONNECT_ARGS, getConnectionArgs(schemes));
		attachments.put(XAConstant.CONTEXT_KEYS.EXECUTE_SQL, sqls.toArray(new String[sqls.size()]));
		attachments.put(XAConstant.CONTEXT_KEYS.ROLLBACK_SQL, rollbacks);
		String txId = UUID.randomUUID().toString();
		XAProposal proposal = factory.createProposal(txId, quorum, attachments);
		coordinator.prepare(proposal, this);
		XARecord record = new XARecord(proposal,null);
		records.put(txId, record);
		synchronized (record) {
			record.wait();
		}
		return record.getExecResult();
	}
	
	private String[][] getConnectionArgs(List<ServerScheme> schemes){
		String[][] args = new String[schemes.size()][];
		for(int i = 0;i < schemes.size();i++){
			ServerScheme scheme = schemes.get(i);
			String[] arg = new String[] {scheme.getHost(),"" + scheme.getPort(),scheme.getAuthority().getUsername(),
					scheme.getAuthority().getPassword()};
			args[i] = arg;
		}
		return args;
	}
	

	@Override
	public void listen(XAResponseSet responses) throws XAException {
		System.out.println("receive" + responses.getCurrentPhase() + "MSG!");
		XARecord record = records.get(responses.getTxID());
		record.setResponse(responses);
		BaseSqlExecResult result;
		if(responses.isPassed()) {
			switch(responses.getCurrentPhase()) {
			case PREPARE:
				coordinator.commit(record.getProposal());
				break;
			case COMMIT:
				result = new BaseSqlExecResult(true);
				record.setExecResult(result);
				synchronized (record) {
					record.notifyAll();
				}
				break;
			case ROLLBACK:
				coordinator.rollback(record.getProposal());
				result = new BaseSqlExecResult(false);
				record.setExecResult(result);
				synchronized (record) {
					record.notifyAll();
				}
			}
		}
		else {
			coordinator.rollback(record.getProposal());
			result = new BaseSqlExecResult(false);
			synchronized (record) {
				record.notifyAll();
			}
		}
	}


}
