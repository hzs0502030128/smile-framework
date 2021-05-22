package org.smile.strate.dispatch;

public class ActionURLInfo {
	/**从uri中解析出的命名空间*/
	protected String namespace;
	/**解析出的action名称*/
	protected String actionName;
	/**uri中的参数值*/
	protected String[] uriArgs;
	
	public ActionURLInfo(String namespace,String actionName){
		this.namespace=namespace;
		this.actionName=actionName;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getActionName() {
		return actionName;
	}

	public String[] getUriArgs() {
		return uriArgs;
	}

	public void setUriArgs(String[] uriArgs) {
		this.uriArgs = uriArgs;
	}
	
}
