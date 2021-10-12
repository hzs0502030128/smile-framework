package org.smile.gateway.invoke;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 方法名一样视为一组方法 
 * @author 胡真山
 *
 */
public class MethodsGroup{
	/**
	 * 参数个数   ，方法列表
	 */
	protected Map<Integer,Set<Method>> methods=new ConcurrentHashMap<Integer,Set<Method>>();
	
	public synchronized void addMethod(Method method){
		Integer argsLen=0;
		Class[] args=method.getParameterTypes();
		if(args!=null){
			argsLen=args.length;
		}
		Set<Method> methodlist=methods.get(argsLen);
		if(methodlist==null){
			methodlist=new HashSet<Method>();
			methods.put(argsLen, methodlist);
		}
		methodlist.add(method);
	}
	
	public Set<Method> getMethodSet(int argsLen){
		return methods.get(argsLen);
	}
}
