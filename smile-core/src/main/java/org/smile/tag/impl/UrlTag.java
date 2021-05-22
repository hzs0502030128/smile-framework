
package org.smile.tag.impl;

import java.net.URLEncoder;

import org.smile.commons.Strings;
import org.smile.commons.ann.Attribute;
import org.smile.tag.Scope;
import org.smile.tag.SimpleTag;
import org.smile.util.StringUtils;

public class UrlTag extends SimpleTag{
	@Attribute
	protected String action;
	@Attribute
	protected String id;
	
	protected StringBuilder url;
	/**是否已经有参数了*/
	protected boolean hasParam=false;
	/***/
	@Attribute
	protected String encode=Strings.UTF_8;
	@Attribute
	protected Scope scope=Scope.page;

	@Override
	public void doTag() throws Exception {
		int index=action.indexOf("?");
		if(index>0){
			hasParam=true;
			url=new StringBuilder(encode(action.substring(0, index), encode));
			url.append("?");
			url.append(encode(action.substring(index+1), encode));
		}else{
			hasParam=false;
			url=new StringBuilder(encode(action, encode));
		}
		invokeBody();
		tagContext.setAttribute(id,url.toString());
	}
	
	protected String encode(String value,String encode) throws Exception{
		return URLEncoder.encode(value,encode);
	}
		
	protected void addParam(String key,Object value) throws Exception{
		if(hasParam){
			url.append("&");
		}else{
			url.append("?");
			hasParam=true;
		}
		if (value instanceof Iterable) {
			value = StringUtils.join((Iterable) value, ';');
		} else if (value instanceof Object[]) {
			value = StringUtils.join((Object[]) value, ';');
		}
		url.append(encode(key,encode)).append("=").append(encode(String.valueOf(value),encode));
	}
}