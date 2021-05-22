package org.smile.validate;

import java.util.Locale;
/**
 * 用于验证框架支持的
 * @author 胡真山
 *
 */
public interface ValidateSupport {
	/**
	 * 提示信息资源文件国际化
	 * @return
	 */
	public Locale locale();
	/**
	 * 添加验证错误信息
	 * @param name 字段名
	 * @param msg 提示信息
	 */
	public void addValidateError(String name, String msg);
	/**
	 * 验证的目标对象
	 * @return
	 */
	public Object getTarget();
}
