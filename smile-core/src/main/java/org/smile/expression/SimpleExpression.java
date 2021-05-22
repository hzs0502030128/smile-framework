package org.smile.expression;


public abstract class SimpleExpression<T> implements Expression<T>{
	//源文本
	protected String source;
	
	//是否not
	protected boolean not=false;

	@Override
	public String getSource() {
		return source;
	}
	/**
	 * 设置源文本
	 * @param source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * 设置内部not
	 * @param not
	 */
	public void setNot(boolean not) {
		this.not = not;
	}
	/***
	 * 设置外部not
	 * @param not
	 */
	public void setWrapNot(boolean not){
		this.not=this.not^not;
	}
	
	@Override
	public T evaluate(){
		return evaluate(null);
	}
	
}
