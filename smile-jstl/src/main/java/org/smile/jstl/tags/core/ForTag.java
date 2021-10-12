
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;

/**
 * For tag simulates simple for loop. For more enhanced looping see {@link org.smile.jstl.tags.core.LoopTag}.
 */
public class ForTag extends LoopingTagSupport {

	@Override
	public void doTag() throws JspException {

		prepareStepDirection();

		loopBody();
	}

}
