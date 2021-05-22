package org.smile.ioc.aware;

/**
 * IOC中处理Aware的工具类
 * @author 胡真山
 * @Date 2016年4月29日
 */
public class AwareHandler {
	/**
	 * 
	 * @param bean
	 * @param beanid
	 */
	public static void handler(Object bean,String beanid){
		if(bean instanceof BeanNameAware){
			((BeanNameAware) bean).setBeanName(beanid);
		}
		if(bean  instanceof InitializingBean){
			((InitializingBean) bean).afterInit();
		}
	}
}
