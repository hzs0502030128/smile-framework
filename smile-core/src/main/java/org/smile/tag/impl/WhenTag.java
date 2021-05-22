
package org.smile.tag.impl;

import org.smile.commons.ann.Attribute;
import org.smile.tag.SimpleTag;
/**
 * <c:when test='${r==1}'>
 * 	<c:then>${address}</c:then>
 * 	<c:other>美国</c:other>
 * </c:when>
 * @author 胡真山
 *
 */
public class WhenTag extends SimpleTag{
	/**条件判断*/
	@Attribute
	private boolean test=false;
	/**
	 * 是否子标签已经处理了body
	 */
	private boolean usedBody=false;

	public boolean isUsedBody() {
		return usedBody;
	}

	public void setUsedBody(boolean usedBody) {
		this.usedBody = usedBody;
	}
	
	@Override
	public void doTag() throws Exception {
		usedBody=false;
		invokeBody();
	}
	
	public void setTest(boolean test){
		this.test=test;
	}
	
	public boolean getTest(){
		return this.test;
	}

	@Override
	protected void reset() {
		super.reset();
		this.usedBody=false;
	}
	
	
}

