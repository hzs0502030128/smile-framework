package org.smile.reflect.asm;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.smile.commons.SmileRunException;
import org.smile.io.IOUtils;
import org.smile.plugin.MethodHashKey;
import org.smile.util.ClassPathUtils;

public class MethodParamReader{
	
	private MethodParamClassVister  vister;
	
	private ClassReader cr;
	
	public MethodParamReader(Class clazz){
	    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		try {
			InputStream is=clazz.getClassLoader().getResourceAsStream(ClassPathUtils.getClassFilePath(clazz));
			byte[] bytes=IOUtils.stream2byte(is);
			cr = new ClassReader(bytes);
			vister=new MethodParamClassVister(cw);
			Method[] methods=clazz.getDeclaredMethods();
			Map<String,List<Method>> namedMethodMap=new HashMap<String,List<Method>>();
			for(Method m:methods){
				String name=m.getName();
				List<Method> method=namedMethodMap.get(name);
				if(method==null){
					method=new LinkedList<Method>();
					namedMethodMap.put(name, method);
				}
				method.add(m);
			}
			vister.namedMethodMap=namedMethodMap;
			reader();
		} catch (IOException e) {
			throw new SmileRunException(clazz.getName(),e);
		}
	}
	
	public void reader(){
		cr.accept(vister, 0);
	}
	
	public String[] getMethodParamName(Method method){
		return vister.paramNamesMap.get(new MethodHashKey(method));
	}
	
	public Map<MethodHashKey,String[]> getMethodParamNames(){
		return vister.paramNamesMap;
	}
}
