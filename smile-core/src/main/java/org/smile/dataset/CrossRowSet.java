package org.smile.dataset;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.smile.commons.NotImplementedException;
import org.smile.dataset.index.IndexAxis;
import org.smile.dataset.index.IndexBuilder;


/**
 * 交叉索引列索引集合
 * @author 胡真山
 *
 */
public class CrossRowSet extends RandomRowSet implements KeyIteratable{
	
    private Map<Key,RowSet> keysCache;
    
    private int[] cols;
    
    public CrossRowSet(DataSet data, Key key, int[] rows, int[] cols2) {
        super(data,rows);
        this.key=key;
        this.cols = cols2;
    }
    
    public Iterator<Key> keys() {
        validate();
        return this.keysCache.keySet().iterator();
    }
    
    
    public Set<Key> keySet(){
    	validate();
    	return this.keysCache.keySet();
    }

    
    public RowSet locate(Key key) {
        validate();
        return (RowSet) keysCache.get(key);
    }

	private void validate() {
		if (keysCache == null) {
            this.keysCache = new IndexBuilder(this, cols).build();
        }
	}

	@Override
	public Iterator<Key> keyIterator() {
		
		return new Iterator<Key>() {
			
			Iterator<Key> iterator=keysCache.keySet().iterator();
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Key next() {
				Key key=iterator.next();
				IndexAxis.indexColumnKey(key);
				return key;
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
			
		};
	}
	
}
