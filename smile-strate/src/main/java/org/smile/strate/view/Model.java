package org.smile.strate.view;

import org.smile.strate.action.ActionElement;
import org.smile.strate.config.ResultConfig;
/**
 * 实现此接口用来定义返回类型
 * @author 胡真山
 *
 * @param <T>
 */
public interface Model<T> {
	/**数据定义类*/
	public Class<T> getDataClass();
	/**获取数据*/
	public T getData();
	/**获取返回配置*/
	public ResultConfig getResultConfig(ActionElement actionElement);
}
