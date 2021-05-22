package org.smile.dataset.group;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.smile.beans.BeanProperties;
import org.smile.beans.PropertiesGetter;
import org.smile.beans.converter.BeanException;
import org.smile.commons.NotImplementedException;
import org.smile.commons.SmileRunException;
import org.smile.dataset.DataSet;
import org.smile.dataset.Key;
import org.smile.dataset.KeyIteratable;
import org.smile.dataset.Row;
import org.smile.dataset.RowSet;
import org.smile.dataset.field.GroupField;
import org.smile.dataset.index.IndexAxis;
import org.smile.dataset.sort.Orderby;
import org.smile.dataset.sort.RowComparator;

public class GroupRowSet implements PropertiesGetter<String, Object>,KeyIteratable{

	private Map<Key,RowSet> keysCacke;
	
	protected Map<String,GroupField> filedCache=new HashMap<String, GroupField>();
	
	private DataSet dataSet;

	public GroupRowSet(RowSet src, Group group) {
		this.dataSet=src.getDataSet();
		group(src, group);
	}

	public void group(RowSet src, Group group) {
		
		Row[] rows = src.toArray();

		int[] cols = new int[1];
		cols[0] = src.getDataSet().getColumnIndex(group.getField());

		if (group.getOrder() != Orderby.ORIGINAL) {
			boolean[] dir = new boolean[] { group.getOrder() == Orderby.ASC };
			RowComparator c = new RowComparator(cols, dir);
			Arrays.sort(rows, c);
		}

		GroupBuilder b = new GroupBuilder(src.getDataSet(), rows, cols[0]);
		this.keysCacke= b.build();
	}
	
	public RowSet locate(Key key){
		return keysCacke.get(key);
	}
	
	public Set<Key> keySet(){
		return this.keysCacke.keySet();	
	}
	
	/**
	 * 生成列字段
	 * @param fieldName
	 * @return
	 */
	public GroupField field(String fieldName) {
		GroupField field=filedCache.get(fieldName);
		if(field==null){
			int col=this.dataSet.getColumnIndex(fieldName);
			field=new GroupField(this, col);
			filedCache.put(fieldName, field);
		}
		return field;
	}

	@Override
	public Object getValue(String name) {
		if(dataSet.getColumnIndex(name)>=0){
			return field(name);
		}
		try {
			return BeanProperties.NORAL_CAN_NO_PROPERTY.getFieldValue(this, name);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public Iterator<Key> keyIterator() {
		return new Iterator<Key>() {
			Iterator<Key> iterator=keysCacke.keySet().iterator();
			@Override
			public boolean hasNext() {
				boolean has= iterator.hasNext();
				if(!has){
					IndexAxis.indexKey(null);
				}
				return has;
			}

			@Override
			public Key next() {
				Key key=iterator.next();
				IndexAxis.indexKey(key);
				return key;
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
		};
	}

}
