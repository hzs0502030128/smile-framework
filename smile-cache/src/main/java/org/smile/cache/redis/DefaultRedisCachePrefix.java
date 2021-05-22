package org.smile.cache.redis;

import org.smile.io.serialize.StringSerializer;
import org.smile.util.StringUtils;


public class DefaultRedisCachePrefix implements RedisCachePrefix {
	
	private final StringSerializer serializer = new StringSerializer();
	
	private final String delimiter;

	public DefaultRedisCachePrefix() {
		this(":");
	}

	public DefaultRedisCachePrefix(String delimiter) {
		this.delimiter = delimiter;
	}

	public byte[] prefix(String managerName,String cacheName) {
		return serializer.serialize(StringUtils.concat(managerName,this.delimiter,cacheName,this.delimiter));
	}

	@Override
	public String keysPrefix(String managerName, String cacheName) {
		return StringUtils.concat(managerName,this.delimiter,cacheName);
	}

	@Override
	public String getDelimiter() {
		return this.delimiter;
	}


}
