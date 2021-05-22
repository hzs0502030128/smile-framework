package org.smile.function;
/**
 * 通用使用函数的接口
 * @author 胡真山
 */
public interface Function {
	/**
	 * 获取函数的值
	 * @param param 函数传入的参数
	 * @return 函数结果
	 */
	public Object getFunctionValue(Object ...args);
	/**
	 * 支持的参数个数
	 * @return
	 */
	public  int getSupportArgsCount();
	/**
	 * 获取函数的名称
	 * @return
	 */
	public String getName();
}
