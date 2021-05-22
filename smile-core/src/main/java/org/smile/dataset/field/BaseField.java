package org.smile.dataset.field;

import org.smile.dataset.Row;
import org.smile.dataset.RowSet;
import org.smile.dataset.index.IndexAxis;

public class BaseField extends AbstractField{
	RowSet rowSet;
	int column;
	
	public BaseField(RowSet rowSet,int col){
		this.rowSet=rowSet;
		this.column=col;
	}
	
	@Override
	public Object[] values() {
		return rowSet.valuesAt(column);
	}

	@Override
	public int getColumn() {
		return this.column;
	}
	
	/**
	 * 字段的值
	 * @return
	 */
	@Override
	public Object value() {
		Row row=IndexAxis.currentRow();
		if(row==null){
			return this.rowSet.valueAt(column);
		}
		return row.value(getColumn());
	}

}
