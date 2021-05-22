package org.smile.beans;
/**
 * 为了兼容javaBean
 * @author 胡真山
 *
 */
public class WrapBean {
	
	private Object myself;
	
	public WrapBean(Object obj){
		this.myself=obj;
	}
	
	public Object getMyself() {
		return myself;
	}
}
