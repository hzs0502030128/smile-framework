package org.smile.dataset.field;

import org.smile.dataset.Key;
import org.smile.dataset.Row;
import org.smile.dataset.RowSet;
import org.smile.dataset.index.IndexAxis;
import org.smile.dataset.index.IndexRowSet;

public class IndexField extends AbstractField{
	/**索引内容*/
	protected IndexRowSet indexView;
	/**索引所在的列*/
	int column;
	
	public IndexField(IndexRowSet indexView,int col){
		this.indexView=indexView;
		this.column=col;
	}
	
	

	@Override
	public Object[] values() {
        RowSet index = this.indexView.locate(getKey());
        if (index != null) {
            return index.valuesAt(column);
        } else {
            return null;
        }
	}

	@Override
	public int getColumn() {
		return column;
	}
	
	protected Key getKey(){
		return IndexAxis.indexKey();
	}
	
	/**
	 * 字段的值
	 * @return
	 */
	@Override
	public Object value() {
		Row row=IndexAxis.currentRow();
		if(row==null){
			RowSet index = this.indexView.locate(getKey());
	        if (index != null) {
	            return index.valueAt(column);
	        } else {
	            return null;
	        }
		}
		return row.value(getColumn());
	}
	
}
