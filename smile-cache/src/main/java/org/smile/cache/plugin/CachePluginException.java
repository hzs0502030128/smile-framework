package org.smile.cache.plugin;

import org.smile.commons.SmileException;

public class CachePluginException extends SmileException{
	public CachePluginException(String msg,Throwable e){
		super(msg,e);
	}
}
