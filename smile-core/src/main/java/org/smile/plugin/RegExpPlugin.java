package org.smile.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.smile.collection.CollectionUtils;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.RegExp;
/**
 * 正则表达式方法名匹配代理方法
 * @author 胡真山
 * @Date 2016年1月26日
 */
public class RegExpPlugin extends PluginSupport implements InvocationHandler {
	/**
	 * 定义方法的类-->类的所有方法
	 * 
	 * 方法缓存
	 */
	protected Map<Class, Map<Method, MethodInterceptor>> signatureMap = new HashMap<Class, Map<Method, MethodInterceptor>>();

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	  Class type=method.getDeclaringClass();
    	  Invocation invocation=new PluginInvocation(this.target, method, args);
    	  Map<Method, MethodInterceptor> methodSet=signatureMap.get(type);
    	  //需要代理的方法，使用方法合理执行
    	  if(CollectionUtils.notEmpty(methodSet)){
    		  MethodInterceptor invocationProxy=methodSet.get(method);
    		  return invocationProxy.intercept(invocation);
    	  }
    	  //使用拦截器的代理方法执行
	      return interceptor.intercept(invocation);
	}

	public static Object wrap(Object target, RegExpInterceptor interceptor) {
		RegExpPlugin plugin=new RegExpPlugin();
		plugin.target=target;
		plugin.interceptor=interceptor;
		Map<RegExp, MethodInterceptor> proxys = interceptor.getMethodInterceptors();
		Class clazz = target.getClass();
		Class[] interfaces = ClassTypeUtils.getAllInterfaces(clazz);
		for (Class targetInterface : interfaces) {
			plugin.initSignatureMap(targetInterface, proxys);
		}
		return Proxy.newProxyInstance(clazz.getClassLoader(), interfaces,plugin);
	}
	
	/**
	 * 初始化方法的代理
	 * @param interfaces
	 * @param methodReg
	 * @param proxy
	 */
	public void initSignatureMap(Class targetInterface,Map<RegExp, MethodInterceptor> proxys){
		Map<Method, MethodInterceptor> methodsMap = new HashMap<Method, MethodInterceptor>();
		Method[] methods = targetInterface.getMethods();
		for (Method m : methods) {
			String methodName = m.getName();
			for (Map.Entry<RegExp, MethodInterceptor> entry : proxys.entrySet()) {
				RegExp methodReg=entry.getKey();
				if (methodReg.test(methodName)&&!methodsMap.containsKey(m)) {
					methodsMap.put(m, entry.getValue());
					break;
				}
			}
		}
		if(CollectionUtils.notEmpty(methodsMap)){
			signatureMap.put(targetInterface, methodsMap);
		}
	}
}
