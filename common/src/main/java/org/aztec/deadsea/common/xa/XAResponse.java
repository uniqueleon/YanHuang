package org.aztec.deadsea.common.xa;

public interface XAResponse {

	public String getID();
	public int getNo();
	public String getHost();
	public boolean isOK();
	public boolean isFail();
	public boolean isMissed();
}
