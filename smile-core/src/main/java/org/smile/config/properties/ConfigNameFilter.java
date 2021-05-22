package org.smile.config.properties;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

import org.smile.collection.CollectionUtils;

public class ConfigNameFilter implements FileFilter {
	
	static Set<String> configName=CollectionUtils.hashSet("_js.cfg","_groovy.cfg",".cfg","");
	private String name;
	public ConfigNameFilter(String name){
		this.name=name;
	}
	
	@Override
	public boolean accept(File pathname) {
		if(pathname.isDirectory()){
			return false;
		}
		String filename=pathname.getName();
		for(String s:configName){
			if(filename.equals(name+s)){
				return true;
			}
		}
		return false;
	}

}
