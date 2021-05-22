package org.smile.db.function;
/***
 * 函数接口可以用于自定义函数
 * @author 胡真山
 *
 */
public interface SqlFunction{
	/***
	 * 	函数名称
	 * @return
	 */
	public String getName();
	
	public Object getAdapted();
}
