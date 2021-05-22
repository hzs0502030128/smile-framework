package org.smile.report.poi;

public interface MergeSet<T> {
	/**
	 * 合并关键值
	 * @param oneData
	 * @return
	 */
	public Object getMergeKeyValue(T oneData);
	/**
	 * 是否需要合并
	 * @param one
	 * @param two
	 * @return
	 */
	public boolean isNeedMerge(Object one,Object two);
	/**
	 * 要合并的列
	 * @return
	 */
	public Integer[] getMergeColumnIndexs();
}
