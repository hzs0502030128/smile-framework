package org.smile.script.groovy;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.io.FileNameUtils;
/**
 * 用于加载groovy文件成class类
 * @author 胡真山
 * @Date 2016年5月10日
 */
public class GroovyLoader {
	
	public static final GroovyLoader DEFAULT=new GroovyLoader();
	
	protected final GroovyClassLoader classLoader = new GroovyClassLoader(ClassLoader.getSystemClassLoader());
	
	public GroovyLoader(){}
	
	private Map<String,Map<String,GroovyClass>> executors=new ConcurrentHashMap<String,Map<String,GroovyClass>>();
	
	protected Set<String> classpaths=new HashSet<String>();
	/**加载时刷新文件*/
	protected boolean loadRefresh=false;
	
	public void addClassPath(String file){
		classpaths.add(file);
	}
	
	public void setLoadRefresh(boolean loadRefresh) {
		this.loadRefresh = loadRefresh;
	}



	public synchronized void addGroovyClass(GroovyClass clazz){
		String _package=clazz.getPackage();
		Map map=executors.get(_package);
		if(map==null){
			map=new ConcurrentHashMap<String,GroovyClass>();
			executors.put(_package, map);
		}
		map.put(clazz.getGroovyClass().getSimpleName(), clazz);
	}
	
	public void loadClasspath(String path){
		loadClasspath(new File(path));
		addClassPath(path);
	}
	
	protected void loadClasspath(File dir){
		File[] files=dir.listFiles();
		for(File f:files){
			if(f.isDirectory()){
				loadClasspath(f);
			}else{
				GroovyClass clazz=new GroovyClass(this);
				clazz.definedClass(f);
				addGroovyClass(clazz);
			}
		}
	}
	/**
	 * 查询类
	 * @param className
	 * @return
	 */
	public GroovyClass findClass(String className){
		GroovyClass clazz= getGroovyClass(className);
		if(clazz!=null){
			return clazz;
		}
		synchronized (classpaths) {
			clazz= getGroovyClass(className);
			if(clazz==null){
				for(String s:classpaths){
					
					clazz=loadOnePackage(s, className);
					if(clazz!=null){
						return clazz;
					}
				}
			}
		}
		return clazz;
	}
	
	protected GroovyClass loadOnePackage(String _package,String className){
		GroovyClass clazz;
		String[] realyname=className.split("\\.");
		StringBuilder name=new StringBuilder();
		for(int i=0;i<realyname.length;i++){
			name.append(realyname[i]);
			if(i==realyname.length-1){
				name.append(".groovy");
			}else{
				name.append(File.separatorChar);
			}
		}
		String fileName=FileNameUtils.concat(_package, name.toString());
		File file=new File(fileName);
		if(file.exists()){
			clazz=new GroovyClass(this);
			clazz.definedClass(file);
			addGroovyClass(clazz);
			return clazz;
		}
		return null;
	}
	
	public GroovyClass getGroovyClass(String clazz){
		String _package;
		String name;
		int index=clazz.lastIndexOf(".");
		if(index>0){
			_package=clazz.substring(0, index);
			name=clazz.substring(index+1);
		}else{
			_package="default";
			name=clazz;
		}
		Map<String,GroovyClass> classMap=executors.get(_package);
		if(classMap==null){
			return null;
		}else{
			GroovyClass groovyClass= classMap.get(name);
			if(loadRefresh){
				groovyClass.refreshFile();
			}
			return groovyClass;
		}
	}
	
	
	public void refreshLoadedClass(){
		for(Map<String,GroovyClass> map:this.executors.values()){
			for(GroovyClass clazz:map.values()){
				clazz.refreshFile();
			}
		}
	}
	
}
