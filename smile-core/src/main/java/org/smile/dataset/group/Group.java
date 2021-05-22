package org.smile.dataset.group;

import org.smile.dataset.sort.Orderby;

public class Group {
	/**分组字段*/
	String field;
	/**排序*/
	Orderby order;
	
	public Group(String field){
		this.field=field;
	}
	
	public Group(String field,Orderby orderby){
		this.field=field;
		this.order=orderby;
	}
	
	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public Orderby getOrder() {
		return order==null?Orderby.ORIGINAL:order;
	}
	
	public void setOrder(Orderby order) {
		this.order = order;
	}
}
