package org.smile.dataset.field;

import org.smile.dataset.Key;
import org.smile.dataset.RowSet;
import org.smile.dataset.group.GroupRowSet;
import org.smile.dataset.index.IndexAxis;

public class GroupField extends AbstractField{
	/**索引内容*/
	protected GroupRowSet groupRowSet;
	/**索引所在的列*/
	int column;
	
	public GroupField(GroupRowSet rowSet,int col){
		this.groupRowSet=rowSet;
		this.column=col;
	}
	
	@Override
	public Object[] values() {
		RowSet index = this.groupRowSet.locate(getKey());
        if (index != null) {
            return index.valuesAt(column);
        } else {
            return null;
        }
	}

	protected Key getKey(){
		return IndexAxis.indexKey();
	}
	
	@Override
	public int getColumn() {
		return this.column;
	}

}
