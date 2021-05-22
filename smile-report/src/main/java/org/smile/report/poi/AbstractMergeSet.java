package org.smile.report.poi;

public abstract class AbstractMergeSet<T> implements MergeSet<T>{
	/**
	 * 要合并的列
	 */
	protected Integer[] mergeColumn;
	/**
	 * 是否需要合并
	 * @param one
	 * @param two
	 * @return
	 */
	public boolean isNeedMerge(Object one,Object two){
		return one.equals(two);
	}

	@Override
	public Integer[] getMergeColumnIndexs() {
		return mergeColumn;
	}

}
