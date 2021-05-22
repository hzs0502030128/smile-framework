package org.smile.util;

import java.util.Enumeration;

public interface Tokenizer<T> extends Enumeration<T>{
	/**
	 * Tests if there are more tokens available from this tokenizer's
	 * @return
	 */
	public boolean hasMoreTokens();
	/**
	 * Returns the next token from this tokenizer
	 * @return
	 */
	public T nextToken();
	/***
	 * 标记记录
	 */
	public void mark();
	/**
	 * 回退出标记的位置
	 */
	public void reset();
}
