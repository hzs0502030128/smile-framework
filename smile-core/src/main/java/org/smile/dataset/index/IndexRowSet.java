package org.smile.dataset.index;

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
import org.smile.dataset.RowSet;
import org.smile.dataset.field.IndexField;



public class IndexRowSet implements PropertiesGetter<String, Object>,KeyIteratable{
	/***
	 * 索引键缓存
	 */
    protected Map<Key,RowSet> keysCache;
    /**
     * 来源数据集
     */
    protected DataSet dataset;
    /**
     * 索引的列
     */
    protected int[] cols;
    
    protected Map<String,IndexField> filedCache=new HashMap<String, IndexField>();
    
    protected IndexRowSet(){}
    
    public IndexRowSet(RowSet src, int[] cols) {
        this.keysCache = new IndexBuilder(src, cols).build();
        this.dataset = src.getDataSet();
        this.cols = cols;
    }
    
    public RowSet locate(Key key) {
        return (RowSet) keysCache.get(key);
    }
    
    public Set<Key> keySet(){
    	return keysCache.keySet();
    }

    
    public DataSet getDataset() {
        return dataset;
    }

	public int[] getCols() {
		return cols;
	}

	/**
	 * 生成列字段
	 * @param fieldName
	 * @return
	 */
	public IndexField field(String fieldName) {
		IndexField field=filedCache.get(fieldName);
		if(field==null){
			int col=this.dataset.getColumnIndex(fieldName);
			field=new IndexField(this, col);
			filedCache.put(fieldName, field);
		}
		return field;
	}

	@Override
	public Object getValue(String name) {
		if(dataset.getColumnIndex(name)>=0){
			return field(name);
		}
		try {
			return BeanProperties.NORAL_CAN_NO_PROPERTY.getFieldValue(this, name);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
	
	public Key getKey(){
		return IndexAxis.indexKey();
	}

	@Override
	public Iterator<Key> keyIterator() {
		
		return new Iterator<Key>() {
			
			Iterator<Key> iterator=keysCache.keySet().iterator();
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
