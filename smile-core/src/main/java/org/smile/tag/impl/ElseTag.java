package org.smile.tag.impl;

public class ElseTag extends IfTag {
	@Override
	public void doTag() throws Exception {
		boolean result = getIfElseSuccess(parent);
		if (!result) {
			invokeBody();
		}
		setIfElseSuccess(parent, true);
	}
}