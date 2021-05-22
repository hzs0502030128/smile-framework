package org.smile.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.smile.reflect.MethodUtils;

public class OverWriteAnnotation {
	/**重写注解的子注解*/
	private Annotation writer;
	/**属性名的映射 */
	private Map<String,String> writeAttributes=new HashMap<>();
	
	protected OverWriteAnnotation(Annotation writer){
		this.writer=writer;
	}
	
	public String getWriteAttribute(String name) {
		return writeAttributes.get(name);
	}
	
	public boolean isOverWrite(String attributeName) {
		return this.writeAttributes.containsKey(attributeName);
	}
	/**
	 * 添加重写属性名称映射
	 * @param target
	 * @param source
	 */
	public void mappingWriteAttribute(String target,String source) {
		this.writeAttributes.put(target, source);
	}
	
	/**
	 * 调用属性方法
	 * @param attributeName
	 * @return
	 */
	public Object invoke(String attributeName) {
		String name=this.writeAttributes.get(attributeName);
		if(name==null) {
			return null;
		}
		Method attributeMethod = MethodUtils.findMethod(writer.annotationType(), name);
		return MethodUtils.invoke(writer,attributeMethod);
	}
}
