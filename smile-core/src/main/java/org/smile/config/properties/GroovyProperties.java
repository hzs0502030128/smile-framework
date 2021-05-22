package org.smile.config.properties;

import org.smile.script.Executor;
import org.smile.script.ScriptType;
import org.smile.script.ScriptExecutor;


public class GroovyProperties extends ScriptProperties{

	protected Executor executor=new ScriptExecutor(getScriptType());
	
	@Override
	protected Object convertConfig(Object obj) {
		return obj;
	}

	@Override
	protected String getScriptType() {
		return ScriptType.GROOVY.value();
	}

	@Override
	protected Executor getExecutor() {
		return executor;
	}
	
	
}
