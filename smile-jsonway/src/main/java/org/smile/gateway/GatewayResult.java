package org.smile.gateway;

public class GatewayResult {
	/**是否成功标记*/
	protected boolean success=true;
	/**返回信息*/
	protected String message;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
