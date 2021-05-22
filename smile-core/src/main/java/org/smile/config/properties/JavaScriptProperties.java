package org.smile.config.properties;

import java.util.Map;

import org.smile.beans.BeanUtils;
import org.smile.json.JSON;
import org.smile.script.Executor;
import org.smile.script.ScriptType;
import org.smile.script.ScriptExecutor;
import org.smile.script.ScriptRunException;

public class JavaScriptProperties extends ScriptProperties {

	private static final String CLASS_KEY = "clazz";

	protected Executor executor = new ScriptExecutor(getScriptType());

	@Override
	protected String getScriptType() {
		return ScriptType.JS.value();
	}

	@Override
	protected Object convertConfig(Object obj) {
		if (obj instanceof Map) {
			Map map = (Map) obj;
			if (map.get(CLASS_KEY) != null) {
				String clazz = String.valueOf(map.get(CLASS_KEY));
				try {
					return BeanUtils.newBean(Class.forName(clazz), map);
				} catch (Exception e) {
					throw new ScriptRunException(e);
				}
			}
		}
		logger.info(JSON.toJSONString(obj));
		return obj;
	}

	@Override
	protected Executor getExecutor() {
		return executor;
	}

}
