
package org.smile.tag.impl;


import org.smile.commons.LoopIterator;
import org.smile.commons.ann.Attribute;
import org.smile.tag.SimpleTag;
public abstract class LoopingTagSupport extends SimpleTag{
	@Attribute
	protected int start;
	@Attribute(required=true)
	protected int end;
	@Attribute
	protected int step = 1;
	@Attribute
	protected String status;
	@Attribute
	protected int modulus = 2;

	/**
	 * Shorter variant of {@link #prepareStepDirection(boolean, boolean)}.
	 */
	protected void prepareStepDirection() {
		if (step == 0) {
			step = (start <= end) ? 1 : -1;
		}
	}

	/**
	 * Prepares step value. If step is 0, it will be set to +1 or -1, depending on start and end value.
	 * <p>
	 * If autoDirection flag is <code>true</code> then it is assumed that step is positive,
	 * and that direction (step sign) should be detected from start and end value.
	 * <p>
	 * If checkDirection flag is <code>true</code> than it checks loop direction (step sign) based on
	 * start and end value. Throws an exception if direction is invalid.
	 * If autoDirection is set, direction checking is skipped.
	 */
	protected void prepareStepDirection(boolean autoDirection, boolean checkDirection) {
		if (step == 0) {
			step = (start <= end) ? 1 : -1;
			return;
		}
		if (autoDirection == true) {
			if (step < 0) {
				throw new IllegalArgumentException("Step value can't be negative: " + step);
			}
			if (start > end) {
				step = -step;
			}
			return;
		}
		if (checkDirection == true) {
			if (start < end) {
				if (step < 0) {
					throw new IllegalArgumentException("Negative step value for increasing loop");
				}
				return;
			}
			if (start > end) {
				if (step > 0) {
					throw new IllegalArgumentException("Positive step value for decreasing loop");
				}
			}
		}
	}

	protected void loopBody() throws Exception {
		LoopIterator loopIterator = new LoopIterator(start, end, step, modulus);
		if (status != null) {
			tagContext.set(status, loopIterator);
		}
		while (loopIterator.next()) {
			invokeBody();
		}
		if (status != null) {
			tagContext.set(status,null);
		}
	}
}
