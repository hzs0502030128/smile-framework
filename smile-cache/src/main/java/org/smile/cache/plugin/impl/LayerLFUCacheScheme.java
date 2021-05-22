package org.smile.cache.plugin.impl;

import org.smile.cache.plugin.DefaultCacheScheme;

public class LayerLFUCacheScheme extends DefaultCacheScheme{
	public LayerLFUCacheScheme(){
		super(new LayerLFUCache(), new LayerKeyGenerator(), new LayerKeyOperator());
	}
}
