package org.smile.strate.action;

import org.smile.commons.Strings;
import org.smile.util.StringUtils;

public class ActionKey {
	/**action名称*/
	private String actionName;
	/**请求的方法*/
	private String requestMethod=Strings.STAR;
	
	public ActionKey(String actionName) {
		this.actionName=actionName;
	}
	
	public ActionKey(String actionName,String requestMethod) {
		this.actionName=actionName;
		this.requestMethod=requestMethod;
	}

	@Override
	public int hashCode() {
		return this.actionName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ActionKey) {
			ActionKey other=(ActionKey)obj;
			if(other.requestMethod==Strings.STAR) {
				return this.actionName.equals(other.actionName);
			}else {
				if(!StringUtils.equals(this.requestMethod, other.requestMethod, true)) {
					return false;
				}
			}
			return this.actionName.equals(other.actionName);
		}
		return false;
	}

	@Override
	public String toString() {
		return requestMethod==null?actionName:actionName+"-"+requestMethod;
	}
	
}
