package org.smile.template;

import java.util.AbstractMap;
import java.util.Set;

import org.smile.commons.SmileRunException;

public abstract class HashTemplateModel extends AbstractMap<String,Object>{
	
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new SmileRunException("not support this method ");
	}
	

	@Override
	public final Object get(Object key) {
		return getModelValue((String)key);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	protected abstract Object getModelValue(String key);
	
}
