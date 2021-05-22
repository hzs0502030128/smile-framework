package org.smile.plugin;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 使用cglig 实现代理
 * 使用CGLib实现动态代理，完全不受代理类必须实现接口的限制
 * 而且CGLib底层采用ASM字节码生成框架，使用字节码技术生成代理类
 * 需要注意的是CGLib不能对声明为final的方法进行代理，因为CGLib原理是动态生成被代理类的子类。
 * @author 胡真山
 *
 */
public class CglibPlugin extends PluginSupport implements MethodInterceptor {

	/**
	 * 拦截目标 产生代理对象
	 * 
	 * @param target 目标
	 * @param interceptor 拦截器
	 * @return 被代理后的对象
	 */
	public static Object wrap(Object target, Interceptor interceptor) {
		CglibPlugin plugin=new CglibPlugin();
		plugin.target=target;
		plugin.interceptor=interceptor;
		return plugin.createProxy(target.getClass());
	}

	/**
	 * 代理方法
	 */
	@Override
	public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		return interceptor.intercept(new PluginInvocation(this.target,method,args));
	}

	/**
	 * 创建代理类对象
	 * @param clazz
	 * @return
	 */
	protected Object createProxy(Class clazz) {
		Enhancer enhancer = new Enhancer(); 
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		return enhancer.create();
	}
}
