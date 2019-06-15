package org.aztec.deadsea.common.xa;

public interface XAPhaseListener {

	public void listen(XAResponseSet responseSet) throws XAException;
}
