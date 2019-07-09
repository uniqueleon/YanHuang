package org.aztec.deadsea.common.xa;

public interface XAResponse {

	public String getId();
	public int getNo();
	public String getHost();
	public boolean isOk();
	public boolean isFail();
	public boolean isMissed();
}
