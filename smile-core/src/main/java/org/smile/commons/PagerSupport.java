package org.smile.commons;

public interface PagerSupport {
	/***
	 * @return 当前页
	 */
	public int getSize();
	/***
	 * @return 跳过记录条数
	 */
	public int getOffset();
	/**
	 * 当前页码
	 * @return
	 */
	public int getPage();
}
