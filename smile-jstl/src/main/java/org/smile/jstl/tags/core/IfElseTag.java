
package org.smile.jstl.tags.core;

import javax.servlet.jsp.tagext.TagSupport;

import org.smile.http.Scopes;

/**
 * 
 */
public class IfElseTag extends TagSupport implements Scopes{
	
	private boolean isSuccess;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}

