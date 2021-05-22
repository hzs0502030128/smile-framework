package org.smile.util;

public abstract class StringTokenizer implements Tokenizer<String>{
	/**查找的文本*/
	protected String text;
	
	@Override
	public boolean hasMoreElements() {
		return this.hasMoreTokens();
	}

	@Override
	public String nextElement() {
		return this.nextToken();
	}
	/**
	 *   在分隔符之后的下一个token
	 * @param separator 查找的分隔符
	 * @return
	 */
	public abstract String nextToken(String separator);
	/**
	 * 当前的分隔符
	 * @return
	 */
	public abstract String separator();
	/**
	 * 跑过空元素
	 */
	public abstract StringTokenizer skipBlank();

}
