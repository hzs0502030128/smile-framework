package org.smile.db.criteria;

import java.io.Serializable;

public interface Criterion extends Serializable{
	/**
	 *     转换成sql语句
	 * @param c
	 * @return
	 */
	public void accept(CriterionVisitor visitor);
}
