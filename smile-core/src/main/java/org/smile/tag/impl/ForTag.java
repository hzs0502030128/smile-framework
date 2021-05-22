
package org.smile.tag.impl;

/**
 * 循环标签 
 * <c:for start='1' end ='10' status='s' step='2'>
 * ${s.count} //记数器 从1开始
 * ${s.index} //索引 从0开始
 * ${s.last} //是否最后一条
 * ${s.first} //是否第一条
 * </c:for>
 * @author 胡真山
 *
 */
public class ForTag extends LoopingTagSupport {

	@Override
	public void doTag() throws Exception {
		prepareStepDirection();
		loopBody();
	}

}
