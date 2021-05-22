package org.smile.cache.plugin.impl;

import org.smile.cache.plugin.CacheKey;
import org.smile.commons.Strings;
import org.smile.util.StringUtils;


public class LayerCacheKey implements CacheKey<String[]>{
	
	protected String value;
	
	protected String[] args;
	/**只使用key匹配 不使用参数匹配*/
	boolean onlyKey=false;

	public LayerCacheKey(String[] args) {
		this.value=StringUtils.join(args, Strings.BLANK);
		this.args=args;
	}
	
	public LayerCacheKey(String key,String paramValues){
		this(new String[]{key,paramValues});
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LayerCacheKey){
			return value.equals(((LayerCacheKey) obj).value);
		}
		return false;
	}
	
	public boolean isOnlyKey() {
		return onlyKey;
	}

	public void onlyKey() {
		this.onlyKey = true;
		this.args=new String[]{args[0]};
	}

	@Override
	public  int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public String[] toKey() {
		return args;
	}
	
}
