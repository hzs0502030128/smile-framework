package org.smile.orm.ioc;

import org.smile.config.BeanCreateException;
import org.smile.ioc.bean.AbstractFactoryBean;
import org.smile.orm.dao.MapperProxy;

/**
 * 接口dao注册IOC的类
 * 
 * @author 胡真山
 * @param <T>
 */
public class MapperFactoryBean<T> extends AbstractFactoryBean<T> {
	/** 接口dao的代理类 */
	private MapperProxy<T> proxy;

	@Override
	public T getObject() throws BeanCreateException {
		if (singleton) {
			return proxy.getInterfaceDao();
		} else {
			return createObject();
		}
	}

	/**
	 * 设置mapper实例代理
	 * @param proxy
	 */
	public void setProxy(MapperProxy<T> proxy) {
		this.proxy = proxy;
	}

	@Override
	public void onRegsitToFactory() {

	}

	@Override
	protected T createObject() throws BeanCreateException {
		return MapperProxy.copy(proxy).getInterfaceDao();
	}

}
