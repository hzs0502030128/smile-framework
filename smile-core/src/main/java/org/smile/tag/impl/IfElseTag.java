
package org.smile.tag.impl;

import org.smile.tag.SimpleTag;
/**
 * 可用于包裹要<if><elseif><else>等标签的外部
 * @author 胡真山
 *
 */
public class IfElseTag extends SimpleTag{
	
	private boolean isSuccess;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	@Override
	protected void reset() {
		super.reset();
		this.isSuccess=false;
	}
	
	
}

