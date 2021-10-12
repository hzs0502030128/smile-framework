package org.smile.gateway;

/**
 * 异常时候返回信息
 * @author 胡真山
 */
public class ErrorGatewayResult extends GatewayResult{
	/**异常详情*/
	private String details;
	/**用于标记异常代码*/
	private String code;
	
	public ErrorGatewayResult(){
		this.success=false;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
}
