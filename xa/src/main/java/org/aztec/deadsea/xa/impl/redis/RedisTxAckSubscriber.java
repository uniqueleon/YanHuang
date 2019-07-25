package org.aztec.deadsea.xa.impl.redis;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.deadsea.common.xa.ProposalFactory;
import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAPhaseListener;
import org.aztec.deadsea.common.xa.XAProposal;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.common.xa.XAResponseBuilder;
import org.aztec.deadsea.xa.impl.SimpleXAResponseSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class RedisTxAckSubscriber implements RedisTxMsgHandler {

	
	private final static Map<String,List<XAResponse>> allResponses = Maps.newConcurrentMap();
	protected boolean unsubscribed = false;
	private final static Map<String,TransactionPhase> lastPhases = Maps.newConcurrentMap();
	
	private final static Stack<XAProposal> notifyStacks = new Stack<XAProposal>();
	private JsonUtils jsonUtil;
	@Autowired
	private List<XAPhaseListener> listeners;
	@Autowired
	private XAResponseBuilder builder;
	
	private final static Map<String,XAProposal> proposals = Maps.newConcurrentMap();

	private Object lockObj = new Object();
	
	private PhaseNotifyThread notifyThread;

	public RedisTxAckSubscriber() {

		jsonUtil = UtilsFactory.getInstance().getJsonUtils();
		ExecutorService es = Executors.newFixedThreadPool(1);
		notifyThread  = new PhaseNotifyThread();
		es.submit(notifyThread);
	}
	

	public void handle(XAProposal proposal,TransactionPhase currentPhase,XAContext context,
			Map<String,Object> dataMap) {
		
		try {
			String txID = proposal.getTxID();
			TransactionPhase lastPhase = currentPhase;
			List<XAResponse> responses = Lists.newArrayList();
			if (allResponses.containsKey(txID)) {
				responses = allResponses.get(txID);
			}
			if (lastPhase != null && !currentPhase.equals(lastPhase)) {
				responses.clear();
			}
			XAResponse response = builder.buildFromMap(proposal.getTxID(), dataMap,currentPhase);
			responses.add(response);
			lastPhases.put(txID, lastPhase);
			synchronized (notifyStacks) {
				notifyStacks.push(proposal);
			}
			synchronized (lockObj) {
				lockObj.notify();
			}
			allResponses.put(txID, responses);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public static String[] getSubscribeChannels(String txID) {
		return new String[] {
				XAConstant.REDIS_CHANNLE_NAMES.PREPARE + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE
						+ XAConstant.REDIS_CHANNLE_NAMES.SEPERATOR + txID,
				XAConstant.REDIS_CHANNLE_NAMES.COMMIT + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE
						+ XAConstant.REDIS_CHANNLE_NAMES.SEPERATOR + txID,
				XAConstant.REDIS_CHANNLE_NAMES.ROLLBACK + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE
						+ XAConstant.REDIS_CHANNLE_NAMES.SEPERATOR + txID,

		};
	}

	public class PhaseNotifyThread implements Callable {

		private boolean runnable = true;

		@Override
		public Object call() throws Exception {

			while (runnable) {
				if(runnable) {
					synchronized (lockObj) {
						lockObj.wait();
					}
				}
				if(!runnable) {
					return null;
				}
				synchronized (notifyStacks) {
					while(!notifyStacks.isEmpty()) {
						XAProposal proposal = notifyStacks.pop();
						TransactionPhase lastPhase = lastPhases.get(proposal.getTxID());
						List<XAResponse> responses = allResponses.get(proposal.getTxID());
						for (XAPhaseListener listener : listeners) {
							listener.listen(
									new SimpleXAResponseSet(lastPhase, proposal.getTxID(), responses, proposal.getQuorum()));
						}
					}
				}
			}
			return null;
		}

		public void stop() {
			this.runnable = false;
			synchronized (lockObj) {
				lockObj.notifyAll();
			}
		}
	}
	
	public void destroy() {
		notifyThread.stop();
	}
	
	public static String[] getChannelPrefix() {
		return new String[] { XAConstant.REDIS_CHANNLE_NAMES.PREPARE + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE, 
				XAConstant.REDIS_CHANNLE_NAMES.COMMIT + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE,
				XAConstant.REDIS_CHANNLE_NAMES.ROLLBACK + XAConstant.REDIS_CHANNLE_NAMES.ACKOWNLEDGE };
	}

	@Override
	public boolean accept(String channel, XAProposal proposal) {
		String[] channelPrefix = getChannelPrefix();
		for(String pref:channelPrefix) {
			if(channel.startsWith(pref)) {
				return true;
			}
		}
		return false;
	}

}
