package org.aztec.deadsea.common.xa;

import java.util.List;

public interface XAPhaseListener {

	public void listen(List<XAResponse> responses) throws XAException;
}
