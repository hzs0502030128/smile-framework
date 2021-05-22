package org.smile.reflect.reader;

import java.lang.reflect.Method;

import javassist.ClassPool;

import org.smile.reflect.javassist.MethodParamReader;
import org.smile.util.SysUtils;

public class JavassistParamNameReader implements ParamNameReader {

	static{
		SysUtils.log("测试是否可以使用:"+ClassPool.class);
	}
	@Override
	public String[] getParameterNames(Method method) {
		return MethodParamReader.getMethodParamNames(method);
	}

}
