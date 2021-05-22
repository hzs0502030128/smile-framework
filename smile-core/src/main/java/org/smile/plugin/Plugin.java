package org.smile.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.smile.beans.converter.BeanException;
import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.commons.ann.Intercept;
import org.smile.commons.ann.Intercepts;
import org.smile.commons.ann.NULL;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;

/***
 * 拦截插件 代理模式拦截 的代理类
 * 
 * @author 胡真山 2015年8月31日
 */
public class Plugin extends PluginSupport implements InvocationHandler {
	/** 要拦截的方法 */
	protected HashMap<Class, Set<Method>> signatureMap;

	protected Plugin(Object target, Interceptor interceptor, HashMap<Class, Set<Method>> methods) {
		this.target = target;
		this.interceptor = interceptor;
		this.signatureMap = methods;
	}
	

	/**
	 * 拦截目标 产生代理对象
	 * 
	 * @param target 目标
	 * @param interceptor 拦截器
	 * @return 被代理后的对象
	 */
	public static Object wrap(Object target, Interceptor interceptor) {
		return wrap(target, interceptor, null);
	}
	
	public static Object wrap(Class type,Interceptor interceptor,HashMap<Class, Set<Method>> methods){
		if (methods == null) {
			//从配置注解中初始化要代理的方法
			methods = getSignatureMap(type, interceptor);
		}
		if (CollectionUtils.notEmpty(methods)){//如果注解也找不到配置则不代理
			Class[] interfaces = ClassTypeUtils.getAllInterfaces(type);
			if (interfaces.length > 0) {
				return Proxy.newProxyInstance(type.getClassLoader(), interfaces, instance(type, interceptor, methods));
			}
		}
		try {
			return ClassTypeUtils.newInstance(type);
		} catch (BeanException e) {
			throw new InvocationException(e);
		}
	}
	
	public static Object wrap(Class type,Interceptor interceptor){
		return wrap(type, interceptor,null);
	}

	/**
	 * 
	 * @param target 目标
	 * @param interceptor 拦截器
	 * @param methods 需要代理的方法
	 * @return
	 */
	public static Object wrap(Object target, Interceptor interceptor, HashMap<Class, Set<Method>> methods) {
		if (methods == null) {
			//从配置注解中初始化要代理的方法
			methods = getSignatureMap(target.getClass(), interceptor);
		}
		if (CollectionUtils.notEmpty(methods)){//如果注解也找不到配置则不代理
			Class type = target.getClass();
			Class[] interfaces = ClassTypeUtils.getAllInterfaces(type);
			if (interfaces.length > 0) {
				return Proxy.newProxyInstance(type.getClassLoader(), interfaces, instance(target, interceptor, methods));
			}
		}
		return target;
	}

	protected static Plugin instance(Object target, Interceptor interceptor, HashMap<Class, Set<Method>> methods) {
		return new Plugin(target, interceptor, methods);
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (signatureMap != null) {
			Class type = method.getDeclaringClass();
			Set<Method> methodSet = signatureMap.get(type);
			if (CollectionUtils.notEmpty(methodSet) && methodSet.contains(method)) {
				return this.interceptor.intercept(new PluginInvocation(this.target, method, args));
			}
		}
		return method.invoke(this.target, args);
	}

	/**
	 * 从注解初始化要拦截的方法
	 * 
	 * @param clazz
	 * @param interceptor
	 * @return
	 */
	private static HashMap<Class, Set<Method>> getSignatureMap(Class clazz, Interceptor interceptor) {
		Intercept interceptAnnotation = (Intercept) interceptor.getClass().getAnnotation(Intercept.class);
		if (interceptAnnotation != null) {//配置注解是从注解中读取
			return getSignatreMap(clazz, interceptAnnotation);
		}
		Intercepts interceptsAnnotation=(Intercepts)interceptor.getClass().getAnnotation(Intercepts.class);
		if(interceptsAnnotation!=null){//从注解集中读取
			return getSignatreMap(clazz, interceptsAnnotation);
		}
		return null;
	}
	/**
	 * 从注解集中读取方法
	 * @param clazz
	 * @param interceptsAnnotation
	 * @return
	 */
	private static HashMap<Class, Set<Method>> getSignatreMap(Class clazz, Intercepts interceptsAnnotation) {
		Intercept[] intercepts=interceptsAnnotation.values();
		if(ArrayUtils.isEmpty(intercepts)){
			//没配置时不代理
			return null;
		}
		HashMap<Class, Set<Method>> methods=new HashMap<Class, Set<Method>>();
		for(Intercept i:intercepts) {
			RegExp methodReg = null;
			Class[] interfaces = null;
			String method = i.method();
			if (StringUtils.notEmpty(method)) {
				methodReg = createMethodReg(method);
			}
			Class targetClass = i.type();
			if (notNullClass(targetClass)) {
				if(targetClass.isAssignableFrom(clazz)){
					interfaces = new Class[] { targetClass };
				}else{
					//如果不是要代理类的接口 则不代理
					continue;
				}
			}else{//没指定接口时 适用所有接口
				interfaces = ClassTypeUtils.getAllInterfaces(clazz);
			}
			Class[] args =getArgs(i);
			HashMap<Class, Set<Method>> submethods =getSignatureMapMap(interfaces, methodReg, args);
			if(CollectionUtils.notEmpty(submethods)){
				for(Map.Entry<Class, Set<Method>> subentry:submethods.entrySet()){
					Set<Method> ms=methods.get(subentry.getKey());
					if(CollectionUtils.isEmpty(ms)){
						methods.put(subentry.getKey(), subentry.getValue());
					}else{
						ms.addAll(subentry.getValue());
					}
				}
			}
		}
		return methods;
	}
	
	/**
	 * 代理方法的参数信息
	 * @param i
	 * @return
	 */
	private static Class[] getArgs(Intercept i){
		if (i.args().length == 1) {
			if (i.args()[0] == NULL.class) {
				return null;
			}
		}
		return i.args();
	}
	/**
	 * 从注解中读取方法
	 * @param clazz
	 * @param interceptsAnnotation
	 * @return
	 */
	private static HashMap<Class, Set<Method>> getSignatreMap(Class clazz, Intercept interceptsAnnotation) {
		RegExp methodReg = null;
		Class[] interfaces = null;
		String method = interceptsAnnotation.method();
		if (StringUtils.notEmpty(method)) {
			methodReg = createMethodReg(method);
		}
		Class targetClass = interceptsAnnotation.type();
		if (notNullClass(targetClass)) {
			if(targetClass.isAssignableFrom(clazz)){
				interfaces = new Class[] { targetClass };
			}else{
				//如果不是要代理类的接口 则不代理
				return null;
			}
		}
		Class[] args =getArgs(interceptsAnnotation);
		if (ArrayUtils.isEmpty(interfaces)) {
			interfaces = ClassTypeUtils.getAllInterfaces(clazz);
		}
		return getSignatureMapMap(interfaces, methodReg, args);
	}

	/**
	 * class 不为空
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean notNullClass(Class clazz) {
		return clazz != null && clazz != NULL.class;
	}

	public static RegExp createMethodReg(String methodReg) {
		return new RegExp(StringUtils.configString2Reg(methodReg));
	}

	/**
	 * 正则表达式过滤要拦截的方法
	 * 
	 * @param interfaces
	 * @param methodReg
	 * @param args
	 * @return
	 */
	public static HashMap<Class, Set<Method>> getSignatureMapMap(Class[] interfaces, RegExp methodReg, Class[] args) {
		HashMap<Class, Set<Method>> methodsMap = new HashMap<Class, Set<Method>>();
		for (Class targetInterface : interfaces) {
			Set<Method> methodsSet = new HashSet<Method>();
			Method[] methods = targetInterface.getMethods();
			for (Method m : methods) {
				String name = m.getName();
				if (methodReg != null && !methodReg.test(name)) {
					continue;
				}
				if (args == null || ArrayUtils.isEquals(args, m.getParameterTypes())) {
					methodsSet.add(m);
				}
			}
			if (CollectionUtils.notEmpty(methodsSet)) {
				methodsMap.put(targetInterface, methodsSet);
			}
		}
		return methodsMap;
	}
}
