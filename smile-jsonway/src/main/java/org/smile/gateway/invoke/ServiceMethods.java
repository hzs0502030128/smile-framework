package org.smile.gateway.invoke;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServiceMethods {
	
	private Class clazz;
	
	private Map<String,MethodsGroup> groups=new HashMap<String, MethodsGroup>();
	
	public ServiceMethods(Class clazz){
		this.clazz=clazz;
	}
	
	protected void init(OpenWayControler controler){
		//初始化方法 
		Method[] methods = controler.initOpenMethods(clazz);
		for (Method m:methods) {
			if(controler.isOpenWay(clazz, m)){
				String name=m.getName();
				MethodsGroup group=groups.get(name);
				if(group==null){
					group=new MethodsGroup();
					groups.put(name, group);
				}
				group.addMethod(m);
			}
		}
	}
	
	public Set<String> getMethodNames(){
		return groups.keySet();
	}
	
	public MethodsGroup getMethodsGroup(String methodName){
		return groups.get(methodName);
	}
}
