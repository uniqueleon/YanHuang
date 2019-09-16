package org.aztec.deadsea.sandbox.ws.dto;

public class ResponseDTO {
	
	private Boolean success;
	private Object data;

	public ResponseDTO() {
		// TODO Auto-generated constructor stub
	}
	
	

	public ResponseDTO(Boolean success, Object data) {
		super();
		this.success = success;
		this.data = data;
	}



	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	

}
