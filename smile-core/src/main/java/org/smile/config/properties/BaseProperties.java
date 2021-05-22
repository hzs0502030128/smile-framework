package org.smile.config.properties;

import org.smile.script.Executor;
import org.smile.script.PropertiesExecutor;
import org.smile.script.ScriptType;

public class BaseProperties extends ScriptProperties{
	
	protected PropertiesExecutor executor=new PropertiesExecutor();
	
	@Override
	protected String getScriptType() {
		return ScriptType.BASE.value();
	}

	@Override
	protected Object convertConfig(Object obj) {
		return obj;
	}

	@Override
	protected Executor getExecutor() {
		return executor;
	}

}
