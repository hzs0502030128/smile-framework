package org.smile.script.groovy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.smile.io.IOUtils;
import org.smile.reflect.Invoker;
import org.smile.script.ScriptRunException;
/**
 * groovy 类型封装
 * @author 胡真山
 * 2015年12月14日
 */
public class GroovyClass {
	/**
	 * 编译后的groovy类
	 */
	private Class groovyClass;
	
	private String _package;
	
	private GroovyLoader groovyLoader;
	
	private String script;
	
	private File file;
	
	private long lastUpdateTime;
	
	protected GroovyClass(GroovyLoader groovyLoader) {
		this.groovyLoader=groovyLoader;
	}
	
	protected synchronized void  definedClass(String script){
		if(!script.equals(this.script)){
			groovyClass = groovyLoader.classLoader.parseClass(script);
			initPackage();
			this.script=script;
		}
	}
	
	protected synchronized void definedClass(File file){
		String code;
		try {
			if(file.lastModified()!=lastUpdateTime){
				lastUpdateTime=file.lastModified();
				this.file=file;
				code = IOUtils.readString(new FileInputStream(file));
				groovyClass = groovyLoader.classLoader.parseClass(code);
				initPackage();
			}
		} catch (IOException e) {
			throw new ScriptRunException(e);
		}
	}
	
	public  synchronized void refreshFile(){
		if(file!=null){
			definedClass(file);
		}
	}

	protected  synchronized void definedClass(InputStream is) {
		String code;
		try {
			code = IOUtils.readString(is);
			definedClass(code);
		} catch (IOException e) {
			throw new ScriptRunException(e);
		}

	}
	
	private void initPackage(){
		String name=groovyClass.getName();
		int index=name.lastIndexOf(".");
		if(index>0){
			_package=name.substring(0, index);
		}else{
			_package="default";
		}
	}

	public Object newInstance() {
		try {
			return groovyClass.newInstance();
		} catch (Exception e) {
			throw new ScriptRunException("实例化" + groovyClass + " newInstance() error", e);
		}
	}

	public Object newInstance(Class[] parameterTypes, Object[] args) {
		try {
			return groovyClass.getConstructor(parameterTypes).newInstance(args);
		} catch (Exception e) {
			throw new ScriptRunException("实例化" + groovyClass + " newInstance error", e);
		}
	}
	
	public Invoker getInvoker(String method,Class... parameters){
		return new Invoker(groovyClass, method, parameters);
	}
	/**
	 * 执行方法
	 * @param method 方法名
	 * @param parameters 方法参数
	 * @param target 目标对象
	 * @param args 参数
	 * @return 方法执行结果
	 */
	public Object invoke(String method,Class[] parameters,Object target,Object[] args){
		return getInvoker(method, parameters).call(target, args);
	}
	/**
	 * 执行无参数的方法
	 * @param method
	 * @param target
	 * @return
	 */
	public Object invoke(String method,Object target){
		return invoke(method, null, target, null);
	}

	public Class getGroovyClass() {
		return groovyClass;
	}

	public String getPackage(){
		return _package;
	}
	
	public  GroovyLoader getGroovyLoader(){
		return groovyLoader;
	}
}
