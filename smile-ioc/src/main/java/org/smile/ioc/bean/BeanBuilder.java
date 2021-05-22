package org.smile.ioc.bean;

import org.smile.beans.BeanProducer;
import org.smile.beans.converter.BeanException;
import org.smile.config.BeanCreateException;
import org.smile.ioc.BeanFactory;
/**
 * 创建Bean的接口  
 * @author 胡真山
 *
 */
public interface BeanBuilder extends BeanProducer{
	/**
	 * 在加载结束时操作
	 * @throws BeanException
	 */
	public void afterLoad() throws BeanException;

	/**注册到工厂时调用*/
	void regsitToFactory(BeanFactory factory) throws BeanCreateException;
}
