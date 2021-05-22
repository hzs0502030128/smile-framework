
package org.smile.tag.impl;

import org.smile.tag.SimpleTag;


public class ThenTag extends SimpleTag {

	@Override
	public void doTag() throws Exception {
		if (parent == null || !(parent instanceof WhenTag)) {
			throw new TagException("Parent WhenTag tag is required", null);
		}
		WhenTag whenTag = (WhenTag) parent;
		if (whenTag.getTest()) {
			whenTag.setUsedBody(true);
			invokeBody();
		}
	}
}