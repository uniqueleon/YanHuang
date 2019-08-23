package org.aztec.deadsea.common.xa;

import java.util.Map;

public interface DistributedTransactionManager extends XAPhaseListener {
	public <T> T submit(String type,int quorum, Map<String, Object> attachments, TransactionResultBuilder<T> builder)
			throws XAException;
	

	public interface TransactionResultBuilder<T>{
		public T buildCommit(XAResponseSet responseSet);
		public T buildRollBack(XAResponseSet responseSet);
	}
}
