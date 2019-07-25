package org.aztec.deadsea.xa.impl.redis.handler;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.deadsea.common.DeadSeaLogger;
import org.aztec.deadsea.common.xa.TransactionPhase;
import org.aztec.deadsea.common.xa.XAConstant;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAPhaseListener;
import org.aztec.deadsea.common.xa.XAProposal;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.common.xa.XAResponseBuilder;
import org.aztec.deadsea.xa.impl.SimpleXAResponseSet;
import org.aztec.deadsea.xa.impl.redis.RedisTxMsgHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

@Component
public class ResponsesHandler implements RedisTxMsgHandler {

	
	private final static Map<String,List<XAResponse>> allResponses = Maps.newConcurrentMap();
	protected boolean unsubscribed = false;
	private final static Map<String,TransactionPhase> lastPhases = Maps.newConcurrentMap();
	
	private final static Queue<XAProposal> notifyQueues = Queues.newConcurrentLinkedQueue();
	private JsonUtils jsonUtil;
	@Autowired
	private List<XAPhaseListener> listeners;
	@Autowired
	private XAResponseBuilder builder;
	
	private final static Map<String,XAProposal> proposals = Maps.newConcurrentMap();
	public final static ExecutorService es = Executors.newFixedThreadPool(1);

	
	private PhaseNotifyThread notifyThread;

	public ResponsesHandler() {

		jsonUtil = UtilsFactory.getInstance().getJsonUtils();
		notifyThread  = new PhaseNotifyThread();
		es.submit(notifyThread);
	}
	

	public void handle(XAProposal proposal,TransactionPhase currentPhase,XAContext context,
			Map<String,Object> dataMap) {
		
		try {
			DeadSeaLogger.info(XAConstant.LOG_KEY, "receive responses for [" + proposal.getTxID() + "]");
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
			notifyQueues.add(proposal);
			allResponses.put(txID, responses);

			DeadSeaLogger.info(XAConstant.LOG_KEY, "Responses for [" + proposal.getTxID() + "] handle finished!");
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
		
		public PhaseNotifyThread() {
			
		}

		@Override
		public Object call() throws Exception {

			while (runnable) {
				try {
					if(!runnable) {
						return null;
					}

						while(!notifyQueues.isEmpty()) {
							XAProposal proposal = notifyQueues.poll();
							DeadSeaLogger.info(XAConstant.LOG_KEY, "Activate listener for [" + proposal.getTxID() + "]");
							TransactionPhase lastPhase = lastPhases.get(proposal.getTxID());
							List<XAResponse> responses = allResponses.get(proposal.getTxID());
							for (XAPhaseListener listener : listeners) {
								listener.listen(
										new SimpleXAResponseSet(lastPhase, proposal.getTxID(), responses, proposal.getQuorum()));
							}
							DeadSeaLogger.info(XAConstant.LOG_KEY, "Activate listener for [" + proposal.getTxID() + "] finished!");
						}
					Thread.currentThread().sleep(1);
				} catch (Exception e) {
					DeadSeaLogger.error(e.getMessage(), e);
				}
			}
			return null;
		}

		public void stop() {
			this.runnable = false;
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
			if(channel.equals(pref)) {
				return true;
			}
		}
		return false;
	}

}
