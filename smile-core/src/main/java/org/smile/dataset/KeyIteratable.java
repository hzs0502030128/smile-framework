package org.smile.dataset;

import java.util.Iterator;

public interface KeyIteratable {
	/**
	 * 对索引键进行迭代
	 * @return
	 */
	public Iterator<Key> keyIterator();
}
