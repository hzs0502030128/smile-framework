package org.smile.script;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptContext;

import org.smile.expression.DefaultContext;

public class ContextBindings extends DefaultContext implements Bindings{

	protected ScriptContext context;
	
	public ContextBindings(ScriptContext context){
		this.context=context;
	}
	
	public ContextBindings(ScriptContext context,Object rootObj) {
		super(rootObj);
		this.context=context;
	}
	
	@Override
	public Object get(String exp) {
		Object value=super.get(exp);
		if(value==null) {
			return context.getAttribute(exp);
		}
		return value;
	}
	@Override
	public int size() {
		return rootValue.size();
	}

	@Override
	public boolean isEmpty() {
		return rootValue.isEmpty();
	}

	@Override
	public boolean containsValue(Object value) {
		return rootValue.containsValue(value);
	}

	@Override
	public void clear() {
		rootValue.clear();
	}

	@Override
	public Set<String> keySet() {
		return rootValue.keySet();
	}

	@Override
	public Collection<Object> values() {
		return rootValue.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return rootValue.entrySet();
	}

	@Override
	public Object put(String name, Object value) {
		return rootValue.put(name, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> toMerge) {
		rootValue.putAll(toMerge);;
	}

	@Override
	public boolean containsKey(Object key) {
		return rootValue.containsKey(key);
	}

	@Override
	public Object get(Object key) {
		return rootValue.get(key);
	}

	@Override
	public Object remove(Object key) {
		return rootValue.remove(key);
	}

}