package org.smile.dataset;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.smile.collection.BaseMapEntry;

public abstract class AbstractRow extends AbstractDataResult implements Row, Map<String, Object> {

	protected RowKey key;
	/** 定义结构元素数 */
	protected DataSetMetaData metaData;
	/**在源dataset中的行索引*/
	protected int index;

	@Override
	public boolean containsKey(Object key) {
		return this.hasColumn((String) key);
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public Object get(Object key) {
		return this.get((String) key);
	}

	@Override
	public Object put(String key, Object value) {
		return set(key, value);
	}

	@Override
	public Object remove(Object key) {
		return set((String) key, null);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new IllegalStateException("not support ");
	}

	@Override
	public void clear() {
		throw new IllegalStateException("not support ");
	}

	@Override
	public Set<String> keySet() {
		return metaData.columnNames();
	}

	@Override
	public Collection<Object> values() {
		throw new IllegalStateException("not support ");
	}

	@Override
	public boolean hasColumn(String columnName) {
		return metaData.getColumnIndex(columnName) > 0;
	}

	@Override
	public int size() {
		return metaData.getColumnCount();
	}

	@Override
	public int getColumnIndex(String columnName) {
		return this.metaData.getColumnIndex(columnName);
	}

	@Override
	public boolean isEmpty() {
		return metaData.getColumnCount() == 0;
	}

	@Override
	public String getColumnName(int column) {
		return this.metaData.getColumnName(column);
	}

	@Override
	public Map<String, Object> readOnlyMap() {
		return new RowWrapMap(this);
	}

	@Override
	public int getColumnCount() {
		return this.metaData.getColumnCount();
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {

		return new AbstractSet<Map.Entry<String, Object>>() {
			@Override
			public Iterator<java.util.Map.Entry<String, Object>> iterator() {

				return new Iterator<Map.Entry<String, Object>>() {
					int idx = 0;

					@Override
					public boolean hasNext() {
						return idx < getColumnCount();
					}

					@Override
					public java.util.Map.Entry<String, Object> next() {
						String keyName = getColumnName(idx);
						Object value = get(idx);
						idx++;
						return new BaseMapEntry<String, Object>(keyName, value);
					}

					@Override
					public void remove() {
						set(idx, null);
					}
				};
			}

			@Override
			public int size() {
				return getColumnCount();
			}
		};
	}

	@Override
	public boolean equals(Row row, int[] columns) {
		return equals(row, columns, columns.length);
	}

	@Override
	public boolean equals(Row row, int[] columns, int size) {
		int count = 0;
		for (int i = 0; i < size; i++) {
			if ((value(columns[i]) != null) && (row.value(columns[i]) != null)) {
				if (value(columns[i]).equals(row.value(columns[i]))) {
					count++;
				}
			} else if ((value(columns[i]) == null) && (row.value(columns[i]) == null)) {
				count++;
			}
		}
		return count == size;
	}

	@Override
	public int index() {
		return index;
	}

	@Override
	public void setIndex(int i) {
		this.index = i;
	}

}
