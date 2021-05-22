package org.smile.reflect.reader;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.ClassReader;
import org.smile.reflect.asm.MethodParamReader;
import org.smile.util.SysUtils;


public class AsmParamNameReader implements ParamNameReader {
	//所有使用过的类的信息缓存
	Map<Class,MethodParamReader> readerMap=new ConcurrentHashMap<Class,MethodParamReader>();
	
	static{
		SysUtils.log("测试是否可以使用："+ClassReader.class);
	}
	
	@Override
	public String[] getParameterNames(Method method) {
		Class clazz=method.getDeclaringClass();
		MethodParamReader reader=readerMap.get(clazz);
		if(reader==null){
			reader=new MethodParamReader(clazz);
			readerMap.put(clazz, reader);
		}
		return reader.getMethodParamName(method);
	}

}
