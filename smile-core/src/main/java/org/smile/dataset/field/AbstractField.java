package org.smile.dataset.field;

import java.util.HashSet;
import java.util.Set;

import org.smile.dataset.FormulaUtils;
import org.smile.dataset.Row;
import org.smile.dataset.index.IndexAxis;

public abstract class AbstractField {

	/**
	 * 字段的所有行的值
	 * @return
	 */
	public abstract Object[] values();

	/**
	 * 汇总
	 * @return
	 */
	public Object sum() {
		return FormulaUtils.sum(values());
	}
	
	public Object getSum(){
		return FormulaUtils.sum(values());
	}

	/**
	 * 最大值
	 * @return
	 */
	public Object max() {
		return FormulaUtils.max(values());
	}
	
	public Object getMax() {
		return FormulaUtils.max(values());
	}

	/**
	 * 最小值
	 * @return
	 */
	public Object min() {
		return FormulaUtils.min(values());
	}
	
	public Object getMin() {
		return FormulaUtils.min(values());
	}

	/**
	 * 取平均
	 * @return
	 */
	public Object avg() {
		return FormulaUtils.avg(values());
	}
	
	public Object getAvg() {
		return FormulaUtils.avg(values());
	}

	/**
	 * 计数
	 * @return
	 */
	public Object count() {
		return FormulaUtils.count(values());
	}
	
	public Object getCount() {
		return FormulaUtils.count(values());
	}

	/**
	 * 最多出现的内容
	 * @return
	 */
	public Object topOccurs() {
		return FormulaUtils.topOccurs(values());
	}
	
	public Object getTopOccurs() {
		return FormulaUtils.topOccurs(values());
	}

	public Object getDistinctCount() {
		return distinctCount();
	}
	/**
	 * 去重记数
	 * @return
	 */
	public Object distinctCount() {
		Object[] vals = values();
		if (vals != null) {
			Set<Object> result = new HashSet<Object>();
			for (int i = 0; i < vals.length; i++) {
				if (!result.contains(vals[i])) {
					result.add(vals[i]);
				}
			}
			return new Integer(result.size());
		} else {
			return new Integer(0);
		}
	}

	public abstract int getColumn();

	public AbstractField getSelf() {
		return this;
	}
	
	/**
	 * 字段的值
	 * @return
	 */
	public Object value() {
		Row row=IndexAxis.currentRow();
		if(row==null){
			return null;
		}
		return row.value(getColumn());
	}

	@Override
	public String toString() {
		return String.valueOf(value());
	}
	
}
