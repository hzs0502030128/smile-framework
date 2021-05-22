package org.smile.db.criteria;

public class OrderbyCriterion implements Criterion{
	
	protected String fieldName;
	
	protected String desc=null;
	
	public OrderbyCriterion(String fieldName) {
		this.fieldName=fieldName;
	}
	
	public OrderbyCriterion(String fieldName,String sort) {
		this.fieldName=fieldName;
		this.desc=sort;
	}
	
	@Override
	public void accept(CriterionVisitor visitor) {
		visitor.visit(this);
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getDesc() {
		return desc==null?"":desc;
	}

	@Override
	public String toString() {
		return desc==null?fieldName:fieldName+" "+desc;
	}

}
