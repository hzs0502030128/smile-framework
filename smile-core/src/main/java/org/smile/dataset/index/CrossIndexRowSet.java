package org.smile.dataset.index;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.smile.dataset.CrossRowSet;
import org.smile.dataset.Key;
import org.smile.dataset.RowSet;
import org.smile.dataset.field.CrossIndexField;
import org.smile.dataset.field.IndexField;
import org.smile.dataset.group.CrossGroupBuilder;


/**
 * 交叉索引展示
 * @author 胡真山
 *
 */
public class CrossIndexRowSet extends IndexRowSet{
	
	protected int[] cols2;
	/**所有列坐标上的key*/
	protected Set<Key> columnKeys;
    
    public CrossIndexRowSet(RowSet src, int[] cols, int[] cols2) {
        this.keysCache = new CrossIndexBuilder(src, cols, cols2).build();
        this.dataset=src.getDataSet();
        this.cols=cols;
        this.cols2=cols2;
    }

    public Iterator<Key> keys() {
        return this.keysCache.keySet().iterator();
    }
    
    public Set<Key> columnKeys(){
    	Set<Key> keys=this.columnKeys;
    	if(keys==null){
    		keys=new LinkedHashSet<Key>();
    		Key star=null;
    		for(RowSet rs:this.keysCache.values()){
    			CrossRowSet crs=(CrossRowSet)rs;
    			for(Key k:crs.keySet()){
    				if(k.isStar()){
    					star=k;
    					continue;
    				}
    				keys.add(k);
    			}
    		}
    		if(star!=null){
    			keys.add(star);
    		}
    		this.columnKeys=keys;
    	}
    	return keys;
    }

    
    public RowSet locate(Key rowKey, Key columnKey){
        CrossRowSet rowset = locate(rowKey);
        if (rowset != null) {
            return rowset.locate(columnKey);
        } else {
            return null;
        }
    }
    @Override
    public Set<Key> keySet(){
    	return this.keysCache.keySet();
    }

    @Override
    public CrossRowSet locate(Key key) {
        CrossRowSet rowset = (CrossRowSet) keysCache.get(key);
        return rowset;
    }

    
    public RowSet[] groups(Key key, Key key2) {
        return CrossGroupBuilder.getDefaults().build(this, key, key2);
    }

	public int[] getCols2() {
		return cols2;
	}
    
	/**
	 * 生成列字段
	 * @param fieldName
	 * @return
	 */
	@Override
	public IndexField field(String fieldName) {
		IndexField field=filedCache.get(fieldName);
		if(field==null){
			int col=this.dataset.getColumnIndex(fieldName);
			field=new CrossIndexField(this, col);
			filedCache.put(fieldName, field);
		}
		return field;
	}
	
	public Key getColumnKey(){
		return IndexAxis.indexColumnKey();
	}
}
