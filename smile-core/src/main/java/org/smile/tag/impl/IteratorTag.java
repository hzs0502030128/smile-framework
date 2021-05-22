
package org.smile.tag.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.type.StringArrayConverter;
import org.smile.collection.BaseMapEntry;
import org.smile.commons.ann.Attribute;
import org.smile.tag.Scope;
import org.smile.tag.SimpleTag;
/**
 * 迭代标签
 * <c:iterator items='${list}' var='s'>
 * 	<c:get name='s'/> 
 *	<name>${s.name}</name><ag>${s.age*5+2}</age>
 *	</c:iterator>
 * @author 胡真山
 *
 */
public class IteratorTag extends SimpleTag {
	@Attribute
	protected Object items;
	/**当前元素的变量名*/
	@Attribute
	protected String var;
	/**当前迭代状态的变量名*/
	@Attribute
	protected String status;
	@Attribute
	protected int modulus = 2;
	@Attribute
	protected Scope scope=Scope.page;
	/**从第向个索引开始循环*/
	@Attribute
	protected int from;
	/**需要循环迭代的元素个数*/
	@Attribute
	protected int count = -1;

	protected IteratorStatus iteratorStatus;

	@Override
	public void doTag() throws Exception {
		if (items == null) {
			return;
		}
		if (!hasBody()) {
			return;
		}

		// create an iterator status if the status attribute was set.
		if (status != null) {
			iteratorStatus = new IteratorStatus(this.modulus);
			tagContext.setAttribute(status, iteratorStatus);
		}
		if (items instanceof Collection) {
			iterateCollection((Collection) items, from, count);
		} else if (items.getClass().isArray()) {
			iterateArray((Object[]) items, from, count);
		} else if (items instanceof String) {
			try {
				iterateArray(new StringArrayConverter().convert(items), from, count);
			} catch (ConvertException e) {
				throw new TagException(e);
			}
		}else if(items instanceof Map){
			iterateEntrySet(((Map)items).entrySet(), from, count);
		} else {
			throw new TagException("Provided items are not iterable");
		}

		// cleanup
		if (status != null) {
			tagContext.removeAttribute(status);
		}
		tagContext.removeAttribute(var);
	}

	/**
	 * Calculates 'TO'.
	 */
	protected int calculateTo(int from, int count, int size) {
		int to = size;
		if (count != -1) {
			to = from + count;
			if (to > size) {
				to = size;
			}
		}
		return to;
	}

	/**
	 * Iterates collection.
	 */
	protected void iterateCollection(Collection collection, int from, int count) throws Exception {
		Iterator iter = collection.iterator();
		int i = 0;
		int to = calculateTo(from, count, collection.size());
		while (i < to) {
			Object item = iter.next();
			if (i >= from) {
				if (status != null) {
					iteratorStatus.next(!iter.hasNext());
				}
				tagContext.setAttribute(var, item);
				invokeBody();
			}
			i++;
		}
	}
	
	/**
	 * Iterates collection.
	 */
	protected void iterateEntrySet(Collection collection, int from, int count) throws Exception {
		Iterator<Map.Entry> iter = collection.iterator();
		int i = 0;
		int to = calculateTo(from, count, collection.size());
		BaseMapEntry entry=new BaseMapEntry();
		tagContext.setAttribute(var, entry);
		while (i < to) {
			Map.Entry item = iter.next();
			if (i >= from) {
				if (status != null) {
					iteratorStatus.next(!iter.hasNext());
				}
				entry.setKey(item.getKey());
				entry.setValue(item.getValue());
				invokeBody();
			}
			i++;
		}
	}

	/**
	 * Iterates arrays.
	 */
	protected void iterateArray(Object[] array, int from, int count) throws Exception {
		int len = array.length;
		int to = calculateTo(from, count, len);
		int last = to - 1;
		for (int i = from; i < to; i++) {
			Object item = array[i];
			if (status != null) {
				iteratorStatus.next(i == last);
			}
			tagContext.setAttribute(var, item);
			invokeBody();
		}
	}

	@Override
	protected void reset() {
		super.reset();
		this.iteratorStatus=null;
	}

	
}
