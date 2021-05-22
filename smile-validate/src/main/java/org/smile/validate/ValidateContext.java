package org.smile.validate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证配置容器
 * @author 胡真山
 */
public class ValidateContext {
	/**
	 * 缓存的验证信息
	 */
	private Map<Class,Map<String,ValidateElement>> validateElements=new ConcurrentHashMap<Class,Map<String,ValidateElement>>();
	/**
	 * 获取验证配置
	 * @param clazz
	 * @param method
	 * @return
	 */
	public ValidateElement getValidateElement(Class clazz,String method){
		Map<String,ValidateElement> clazzMap=validateElements.get(clazz);
		if(clazzMap==null){
			synchronized (clazz) {
				clazzMap=validateElements.get(clazz);
				if(clazzMap==null){
					clazzMap=initOneClassValidate(clazz);
					validateElements.put(clazz, clazzMap);
				}
			}
		}
		return clazzMap.get(method);
	}
	/**
	 * 对一个数据对象进行验证
	 * @param clazz
	 * @param method
	 * @param v
	 * @return
	 */
	public boolean validate(Class clazz,String method,ValidateSupport v){
		ValidateElement ve=getValidateElement(clazz, method);
		if(ve!=null){
			return ve.validate(v);
		}
		return true;
	}
	
	/**
	 * 初始化一个类的验证配置
	 * @param clazz
	 * @return
	 */
	public Map<String,ValidateElement> initOneClassValidate(Class clazz){
		Map<String,ValidateElement> map=new HashMap<String, ValidateElement>();
		Method[] methods=clazz.getMethods();
		for(Method m:methods){
			String name=m.getName();
			ValidateElement ve=ValidateElementUtils.checkAnnotationValidate(m);
			if(ve!=null){
				map.put(name, ve);
			}
		}
		return map;
	}
	
}
