package org.smile.expression;



public interface ExpressionEngine{
	/**
	 * 
	 * @param context
	 * @param expression
	 * @return
	 */
	public Object evaluate(Context context,String expression);
	
	
	/**
	 * 创建解析上下文
	 * @param rootValue
	 * @return
	 */
	public Context createContext(Object rootValue);
}
