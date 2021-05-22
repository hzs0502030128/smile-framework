package org.smile.reflect.javassist;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;

import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.log.LoggerHandler;
import org.smile.util.ClassPathUtils;
import org.smile.util.SysUtils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class MethodParamReader implements LoggerHandler{
	
	static ClassPool pool =ClassPool.getDefault();
	static{
    	try {
    		Set<String> files=ClassPathUtils.getClassPathDirAndJar();
    		if(CollectionUtils.notEmpty(files)) {
    			for(String f:files) {
    				pool.appendClassPath(f);
    			}
    		}
		} catch (NotFoundException e) {
			SysUtils.log("不支持javassist",e);
		}
	}
    /**
     * 
     * <p>
               * 获取方法参数名称
     * </p>
     * 
     * @param cm
     * @return
     */
    protected static String[] getMethodParamNames(CtMethod cm) {
        CtClass cc = cm.getDeclaringClass();
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        String[] paramNames = null;
        if(codeAttribute!=null){
	        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute .getAttribute(LocalVariableAttribute.tag);
	        if (attr == null) {
	            throw new SmileRunException(cc.getName());
	        }
	        try {
	            paramNames = new String[cm.getParameterTypes().length];
	        } catch (NotFoundException e) {
	            throw new SmileRunException(e);
	        }
	        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
	        TreeMap<Integer, String> sortMap = new TreeMap<Integer, String>();
	        for (int i = 0; i < attr.tableLength(); i++){
	            sortMap.put(attr.index(i), attr.variableName(i));
	        }
	        paramNames = Arrays.copyOfRange(sortMap.values().toArray(new String[paramNames.length]), pos, paramNames.length + pos);
        }
        return paramNames;
    }
 
    /**
                * 获取方法参数名称，按给定的参数类型匹配方法
     * 
     * @param clazz
     * @param method
     * @param paramTypes
     * @return
     */
    public static String[] getMethodParamNames(Class<?> clazz, String method,Class<?>... paramTypes) {
        CtClass cc = null;
        CtMethod cm = null;
        try {
            cc = pool.get(clazz.getName());
            //参数类型名称
            String[] paramTypeNames = new String[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++){
                paramTypeNames[i] = paramTypes[i].getName();
            }
            CtClass[] classes = pool.get(paramTypeNames);
            cm = cc.getDeclaredMethod(method,classes);
        } catch (NotFoundException e) {
			throw new SmileRunException("获取方法"+method+"的参数名称失败",e);
        }
        return getMethodParamNames(cm);
    }
    /**
               * 获取方法的方法名
     * @param clazz
     * @param method
     * @return
     */
    public static String[] getMethodParamNames(Class<?> clazz,Method method){
    	return getMethodParamNames(clazz, method.getName(), method.getParameterTypes());
    }
    
    public static String[] getMethodParamNames(Method method){
    	return getMethodParamNames(method.getDeclaringClass(), method);
    }
    /**
     * 获取方法参数名称，匹配同名的某一个方法
     * 
     * @param clazz
     * @param method
     * @return
     * @throws NotFoundException
     *             如果类或者方法不存在
     * @throws MissingLVException
     *             如果最终编译的class文件不包含局部变量表信息
     */
    public static String[] getMethodParamNames(Class<?> clazz, String method) {
        CtClass cc;
        CtMethod cm = null;
        try {
            cc = pool.get(clazz.getName());
            cm = cc.getDeclaredMethod(method);
        } catch (NotFoundException e) {
        	 throw new SmileRunException(e);
        }
        return getMethodParamNames(cm);
    }
 
 
}
