package org.smile.cache.plugin.impl;

import java.util.Collection;
import java.util.List;

import net.sf.ehcache.Element;

import org.smile.cache.CacheModel;
import org.smile.cache.ecache.BaseEcache;
import org.smile.util.StringUtils;


public class LayerEhcache<V> extends BaseEcache<String[], V>{
	
	protected final String SPLIT="$";
	
	protected String toKey(String[] key){
		return StringUtils.join(key, SPLIT);
	}
	
	@Override
	public void put(String[] key, V object, long timeout) {
		this.ecache.put(new Element(toKey(key),object,0,convertTime(timeout)));
	}
	
	@Override
	public void put(String[] key, V object) {
		this.ecache.put(new Element(toKey(key),object));
	}
	
	@Override
	public V get(String[] key) {
		Element ele=ecache.get(toKey(key));
		if(ele==null){
			return null;
		}
		return (V) ele.getObjectValue();
	}
	@Override
	public void remove(String[] key) {
		ecache.remove(toKey(key));
	}
	
	@Override
	public void put(CacheModel<String[], V> model, long timeout) {
		this.put(model.getKey(), model.getObject(), timeout);
	}
	
	@Override
	public void remove(Collection<String[]> keys) {
		for(String[] key:keys){
			this.remove(key);
		}
	}
	
	public void removeNode(String[] node){
		List<String> list=ecache.getKeys();
		for(String str:list){
			if(str.startsWith(toKey(node)+SPLIT)){
				ecache.remove(str);
			}
		}
	}
}
