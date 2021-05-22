package org.smile.cache.plugin.impl;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.smile.cache.plugin.CacheKey;
import org.smile.collection.ArrayUtils;
import org.smile.commons.StringBand;
import org.smile.commons.Strings;

public class BeanCacheKey implements CacheKey<BeanCacheKey> {
	/** 调用目标对象全类名 */
	private String targetClassName;
	/** 调用目标方法名称 */
	private String methodName;
	/** 调用目标参数 */
	private Object[] params;
	/**生成的hashcode*/
	private final int hashCode;
	
	public BeanCacheKey(Object target, Method method, Object[] elements) {
		this.targetClassName = target.getClass().getName();
		this.methodName = generatorMethodName(method);
		if (ArrayUtils.notEmpty(elements)) {
			this.params = new Object[elements.length];
			for (int i = 0; i < params.length; i++) {
				Object ele = elements[i];
				this.params[i] = ele;
			}
		}
		this.hashCode = generatorHashCode();
	}

	private String generatorMethodName(Method method) {
		StringBuilder builder = new StringBuilder(method.getName());
		Class<?>[] types = method.getParameterTypes();
		builder.append("(");
		if (ArrayUtils.notEmpty(types)) {
			for (int i=0;i<types.length;i++) {
				builder.append(types[i].getName());
				if(i<types.length-1){
					builder.append(",");
				}
			}
		}
		builder.append(")");
		return builder.toString();
	}

	/**
	 * 生成hashCode 
	 * @return cachekey的hashcode
	 */
	private int generatorHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hashCode;
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + Arrays.hashCode(params);
		result = prime * result + ((targetClassName == null) ? 0 : targetClassName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeanCacheKey other = (BeanCacheKey) obj;
		if (hashCode != other.hashCode)
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (!Arrays.equals(params, other.params))
			return false;
		if (targetClassName == null) {
			if (other.targetClassName != null)
				return false;
		} else if (!targetClassName.equals(other.targetClassName))
			return false;
		return true;
	}

	@Override
	public final int hashCode() {
		return hashCode;
	}

	@Override
	public BeanCacheKey toKey() {
		return this;
	}
	
	@Override
	public String toString(){
		StringBand str=new StringBand();
		str.append(targetClassName).append(Strings.DOT).append(methodName).append(params);
		return str.toString();
	}
}
