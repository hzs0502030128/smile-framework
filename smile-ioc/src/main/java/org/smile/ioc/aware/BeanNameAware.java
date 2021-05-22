package org.smile.ioc.aware;
/**
 * 需要处理bean名称的接口
 * @author 胡真山
 * @Date 2016年4月29日
 */
public interface BeanNameAware {
	/**
	 * 设置bean的名称
	 * @param name
	 */
	public void setBeanName(String name);
}
