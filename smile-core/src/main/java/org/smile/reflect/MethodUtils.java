package org.smile.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.collection.SoftHashMap;
import org.smile.commons.SmileRunException;

public class MethodUtils {
	
	private static Map<Class,Method[]> declaredMethodsCache=SoftHashMap.newConcurrentInstance();
	/***空方法数组*/
	private static Method[] EMPTYS=new Method[0];
	private static Object[] EMPTY_ARGS=new Object[0];
	/**
	 * 获取方法 没有此方法的时候反回null
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getMethod(Class clazz,String methodName,Class ... parameterTypes){
		try{
			return clazz.getMethod(methodName, parameterTypes);
		}catch(NoSuchMethodException e){
			return null;
		}
	}
	/**
	 * 获取定义的方法 没有的时候返回null
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getDeclaredMethod(Class clazz,String methodName,Class ... parameterTypes){
		try {
			return clazz.getDeclaredMethod(methodName, parameterTypes);
		}catch (NoSuchMethodException e) {
			return null;
		}
	}
	/**
	 * 从所有的方法中查找 包括定义的方法和公有的方法
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getDeclaredAndPublicMethod(Class clazz,String methodName,Class ... parameterTypes){
		try {
			return clazz.getDeclaredMethod(methodName, parameterTypes);
		}catch (NoSuchMethodException e) {
			return getMethod(clazz, methodName, parameterTypes);
		}
	}
	/**
	 * 循环所有父类 获取方法
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getAnyMethod(Class clazz,String methodName,Class ... parameterTypes){
		do{
			try{
				return clazz.getDeclaredMethod(methodName, parameterTypes);
			}catch(NoSuchMethodException e){}
		}while((clazz=clazz.getSuperclass())!=Object.class);
		return null;
	}
	/**
	 * 	按方法名称获取方法 所有public方法 
	 * @param clazz 类名
	 * @param methodName 方法名
	 * @return
	 */
	public static List<Method> getMethodByName(Class clazz,String methodName){
		Method[] methods=clazz.getMethods();
		List<Method> args=new LinkedList<Method>();
		for(Method m:methods){
			if(m.getName().equals(methodName)){
				args.add(m);
			}
		}
		return args;
	}
	
	/**
	 * 	按方法名称和参数个数获取方法
	 * 所有的public方法
	 * @param clazz 类
	 * @param methodName 方法名称
	 * @param paramCout 参数个数
	 * @return
	 */
	public static List<Method> getMethodByNameAndParamCount(Class clazz,String methodName,int paramCout){
		Method[] methods=clazz.getMethods();
		List<Method> resMethods=new ArrayList<Method>(methods.length);
		for(Method m:methods){
			if(m.getName().equals(methodName)&&m.getParameterTypes().length==paramCout){
				resMethods.add(m);
			}
		}
		return resMethods;
	}
	/**
	 * 设置方法 
	 * @param target 调用对象
	 * @param method 方法
	 * @param args 方法参数
	 * @return
	 */
	public static Object invoke(Object target,Method method,Object... args){
		try {
			return method.invoke(target, args);
		} catch (Exception e) {
			throw new SmileRunException(method.getName(),e);
		}
	}
	
	/**
	 * 静态方法调用
	 * @param method
	 * @param args
	 * @return
	 */
	public static Object staticInvoke(Method method,Object... args){
		try {
			return method.invoke(null, args);
		} catch (Exception e) {
			throw new SmileRunException(method.getName(),e);
		}
	}
	/**
	 * 获取一个类的所有方法，包括父类的方法以父类私有方法
	 * @param clazz
	 * @return
	 */
	public static Set<Method> getAllDeclaredMethods(Class clazz){
		Set<Method> methods=new LinkedHashSet<>();
		doWithMethods(clazz, methods);
		return methods;
	}
	
	/**
	 * 	递归处理一个类定义的所有方法 放入到set中
	 * @param clazz
	 * @param results
	 */
	private  static void doWithMethods(Class<?> clazz, Set<Method> results) {
		// Keep backing up the inheritance hierarchy.
		Method[] methods = getDeclaredMethods(clazz);
		for (Method method : methods) {
			results.add(method);
		}
		if (clazz.getSuperclass() != null) {
			doWithMethods(clazz.getSuperclass(), results);
		}else if (clazz.isInterface()) {
			for (Class<?> superIfc : clazz.getInterfaces()) {
				doWithMethods(superIfc,results);
			}
		}
	}
	/**
	 * 获取一个类定义的所有方法 
	 * 包括接口中的非抽象方法
	 * @param clazz
	 * @return
	 */
	public static Method[] getDeclaredMethods(Class<?> clazz) {
		Method[] result = declaredMethodsCache.get(clazz);
		if (result == null) {
			try {
				Method[] declaredMethods = clazz.getDeclaredMethods();
				List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
				if (defaultMethods != null) {
					result = new Method[declaredMethods.length + defaultMethods.size()];
					System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
					int index = declaredMethods.length;
					for (Method defaultMethod : defaultMethods) {
						result[index] = defaultMethod;
						index++;
					}
				}
				else {
					result = declaredMethods;
				}
				declaredMethodsCache.put(clazz, (result.length == 0 ? EMPTYS : result));
			}
			catch (Throwable ex) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
						"] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
			}
		}
		return result;
	}

	/**
	 * 获取一个类接口中不是抽象的方法
	 * @param clazz
	 * @return
	 */
	public  static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
		List<Method> result = null;
		for (Class<?> ifc : clazz.getInterfaces()) {
			for (Method ifcMethod : ifc.getMethods()) {
				//不是abstract方法
				if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
					if (result == null) {
						result = new LinkedList<>();
					}
					result.add(ifcMethod);
				}
			}
		}
		return result;
	}
	
	public static void makeAccessible(Method method) {
		method.setAccessible(true);
	}
	
	/***
	 * 查找所有方法
	 * @param clazz
	 * @param name
	 * @param paramTypes
	 * @return
	 */
	public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
			for (Method method : methods) {
				if (name.equals(method.getName()) &&
						(paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	/**
	 * Determine whether the given field is a "public static final" constant.
	 * @param field the field to check
	 */
	public static boolean isPublicStaticFinal(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}

	/**
	 * Determine whether the given method is an "equals" method.
	 * @see java.lang.Object#equals(Object)
	 */
	public static boolean isEqualsMethod(Method method) {
		if (method == null || !method.getName().equals("equals")) {
			return false;
		}
		Class<?>[] paramTypes = method.getParameterTypes();
		return (paramTypes.length == 1 && paramTypes[0] == Object.class);
	}

	/**
	 * Determine whether the given method is a "hashCode" method.
	 * @see java.lang.Object#hashCode()
	 */
	public static boolean isHashCodeMethod(Method method) {
		return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
	}

	/**
	 * Determine whether the given method is a "toString" method.
	 * @see java.lang.Object#toString()
	 */
	public static boolean isToStringMethod(Method method) {
		return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
	}

	/**
	 * Determine whether the given method is originally declared by {@link java.lang.Object}.
	 */
	public static boolean isObjectMethod(Method method) {
		if (method == null) {
			return false;
		}
		try {
			Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

}
