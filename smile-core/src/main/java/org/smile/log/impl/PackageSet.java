package org.smile.log.impl;

import java.util.HashSet;
import java.util.Set;

import org.smile.log.Logger.Level;

public class PackageSet {
	
	public Level level;
	
	public Set<String> loggerNames;
	
	public PackageSet(String packageSet){
		String[] splits=packageSet.trim().split(",");
		if(splits.length>0){
			level=Level.toLevel(splits[0]);
		}
		if(splits.length>1){
			loggerNames=new HashSet<String>();
			for(int i=1;i<splits.length;i++){
				loggerNames.add(splits[i]);
			}
		}
	}
	
	public Level getLevel() {
		return level;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	public Set<String> getLoggerNames() {
		return loggerNames;
	}
}
