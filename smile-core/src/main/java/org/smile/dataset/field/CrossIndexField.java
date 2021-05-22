package org.smile.dataset.field;

import org.smile.dataset.CrossRowSet;
import org.smile.dataset.Key;
import org.smile.dataset.Row;
import org.smile.dataset.RowSet;
import org.smile.dataset.index.IndexAxis;
import org.smile.dataset.index.IndexRowSet;

public class CrossIndexField extends IndexField{
	
	public CrossIndexField(IndexRowSet indexView, int col) {
		super(indexView, col);
	}
	
	@Override
	public Object[] values() {
        RowSet rowset = locate(this.getKey(), this.getColumnKey());
        if (rowset != null) {
            return rowset.valuesAt(column);
        }
        return null;
	}

	@Override
	public int getColumn() {
		return column;
	}

	public CrossRowSet locate(Key key){
		return (CrossRowSet) this.indexView.locate(key);
	}
	
	public RowSet locate(Key key,Key key2){
		CrossRowSet crt= (CrossRowSet) this.indexView.locate(key);
		if(crt==null){
			return null;
		}
		return crt.locate(key2);
	}
	
	protected Key getColumnKey(){
		return IndexAxis.indexColumnKey();
	}
	
	/**
	 * 字段的值
	 * @return
	 */
	@Override
	public Object value() {
		Row row=IndexAxis.currentRow();
		if(row==null){
			RowSet index =locate(getKey(), getColumnKey());
	        if (index != null) {
	            return index.valueAt(column);
	        } else {
	            return null;
	        }
		}
		return row.value(getColumn());
	}
}
