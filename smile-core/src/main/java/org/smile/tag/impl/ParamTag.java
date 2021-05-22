
package org.smile.tag.impl;

import org.smile.commons.ann.Attribute;
import org.smile.io.StringWriter;
/**
 * url tag 内部标签的父类
 * @author 胡真山
 *
 */
public class ParamTag extends UrlTagSupport{
	@Attribute
	private String name;
	@Attribute
	private Object value;
	
	@Override
	public void doTag() throws Exception {
		UrlTag urlTag=getUrlTag();
		if(value==null){
			if(this.hasBody()){
				StringWriter writer=new StringWriter();
				invokeBody(writer);
				value=writer.toString();
			}
		}
		urlTag.addParam(name, value);
	}
	/**
	 * 重置参数
	 */
	protected void reset(){
		this.value=null;
	}
}