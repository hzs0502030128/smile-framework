package org.smile.tag.impl;

import org.smile.tag.SimpleTag;
/**
 * switch 字标签  case 后的default
 * @author 胡真山
 * 2015年11月5日
 */
public class DefaultTag extends SimpleTag{

	@Override
	public void doTag() throws Exception {
		if (parent == null || !(parent instanceof SwitchTag)) {
			throw new TagException(SwitchTag.MSG_PARENT_SWITCH_REQUIRED, null);
		}
		SwitchTag switchTag = (SwitchTag) parent;
		if (!switchTag.isCased()) {
			invokeBody();
			switchTag.cased();
		}
	}
}