package org.smile.tag.impl;

import org.smile.commons.ann.Attribute;
import org.smile.tag.SimpleTag;

/**
 * 配合switch标签使用 在switch内部
 * <switch value="${value}">
 * 	<case value="A" ></case>
 * 	<case value="B" ></case>
 * 	<case value="C" ></case>
 * </switch>
 * @author 胡真山
 *
 */
public class CaseTag extends SimpleTag {
	@Attribute
	private Object value;
	
	@Override
	protected void doTag() throws Exception {
		if (parent == null || !(parent instanceof SwitchTag)) {
			throw new TagException(SwitchTag.MSG_PARENT_SWITCH_REQUIRED, null);
		}
		SwitchTag switchTag = (SwitchTag) parent;
		if ((switchTag.getValue() != null) && switchTag.getValue().equals(value)) {
			switchTag.cased();
			invokeBody();
		}
	}
}
