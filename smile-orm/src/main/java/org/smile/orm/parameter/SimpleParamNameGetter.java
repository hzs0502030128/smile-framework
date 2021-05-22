package org.smile.orm.parameter;

import java.lang.reflect.Method;

import org.smile.reflect.reader.SimpleParamNameReader;
/**
 * 从方法 的注解中获取参数的名称
 * @author 胡真山
 *
 */
public class SimpleParamNameGetter implements ParamNameGetter {
	
	private static SimpleParamNameReader reader=new SimpleParamNameReader();
	
	@Override
	public String[] getParamName(Method method){
		String[] names=reader.getParameterNames(method);
		return names;
	}

}
