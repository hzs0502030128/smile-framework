package org.smile.dataset;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.smile.collection.BaseMapEntry;
/**
 * 把行数据包装成一个map来读取数据
 * @author 胡真山
 *
 */
public class RowWrapMap extends AbstractMap<String,Object>{
	/**数据来源行*/
	protected Row row;
	
	public RowWrapMap(Row row){
		this.row=row;
	}
	
	@Override
	public Object get(Object key) {
		return row.get((String)key);
	}

	@Override
	public Object put(String key, Object value) {
		return row.set(key, value);
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		
		return new AbstractSet<Map.Entry<String,Object>>() {
			@Override
			public Iterator<java.util.Map.Entry<String, Object>> iterator() {
				
				return new Iterator<Map.Entry<String,Object>>() {
					int idx=0;
					@Override
					public boolean hasNext() {
						return idx<row.getColumnCount();
					}

					@Override
					public java.util.Map.Entry<String, Object> next() {
						String keyName=row.getColumnName(idx);
						Object value=row.get(idx);
						idx++;
						return new BaseMapEntry<String,Object>(keyName, value);
					}

					@Override
					public void remove() {
						row.set(idx, null);
					}
				};
			}

			@Override
			public int size() {
				return row.getColumnCount();
			}
		};
	}

}
