
package org.smile.tag.impl;

import org.smile.commons.ann.Attribute;
import org.smile.tag.Fragment;
import org.smile.tag.SimpleTag;
import org.smile.tag.parser.TagFragment;
/**
 * <c:switch value='${r}'>
 * 	<c:case value='3'>${address}</c:case>
 * 	<c:case value='1'>日本</c:case>
 * 	<c:default>美国</c:default>
 * </c:switch>
 * @author 胡真山
 *
 */
public class SwitchTag extends SimpleTag{

	static final String MSG_PARENT_SWITCH_REQUIRED = "Parent switch tag is required.";

	@Attribute
	private String value;
	//
	private boolean isCase;
	
	public String getValue() {
		return value;
	}

	/***
	 * 已匹配使用
	 */
	public void cased() {
		isCase = true;
	}

	public boolean isCased() {
		return isCase;
	}

	@Override
	public void doTag() throws Exception {
		for(Fragment f:this.tagFragment.getSubFragment()){
			if(f.isTag()){
				if(!this.isCase){
					((TagFragment)f).invoke(tagContext,this);
				}
			}else{
				f.invoke(tagContext);
			}
		}
	}

	@Override
	protected void reset() {
		super.reset();
		this.value=null;
		this.isCase=false;
	}

	
}
