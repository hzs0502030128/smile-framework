package org.smile.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.smile.beans.FieldDeclare;
import org.smile.beans.MapBeanClass;
/**
 * 把方法的参数封装成一个类
 * 参数名作为类的定义字段
 * @author 胡真山
 */
public class MethodParamBeanClass extends MapBeanClass{
	/**方法信息*/
	private MethodArgs methodArgs;
	
	public MethodParamBeanClass(MethodArgs args){
		this.methodArgs=args;
		String[] paramNames=args.getParamNames();
		Type[] types=args.getMethod().getGenericParameterTypes();
		if(paramNames.length>0){
			for(int i=0;i<paramNames.length;i++){
				Type type=types[i];
				FieldDeclare fDeclare=null;
				if(type instanceof Class){
					fDeclare=new FieldDeclare((Class)type);
				}else if(type instanceof ParameterizedType){
					Class paramType=(Class)((ParameterizedType)type).getRawType();
					Generic g=ClassTypeUtils.getGenericObj(type);
					fDeclare=new FieldDeclare(paramType, g);
				}
				this.declareFiled(paramNames[i], fDeclare);
			}
		}
	}
	/**
	 * @param method
	 */
	public MethodParamBeanClass(Method method){
		this(new MethodArgs(method));
	}
	
	/**
	 * 方法参数对象
	 * @return
	 */
	public MethodArgs getMethodArgs() {
		return methodArgs;
	}
	
	/**
	 * 当前类封装的方法
	 * @return
	 */
	public Method getMethod(){
		return methodArgs.getMethod();
	}
	
}
