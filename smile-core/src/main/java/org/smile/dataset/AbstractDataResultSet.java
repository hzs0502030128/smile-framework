package org.smile.dataset;

import org.smile.dataset.group.Group;
import org.smile.dataset.group.GroupRowSet;
import org.smile.dataset.index.CrossIndexRowSet;
import org.smile.dataset.index.IndexRowSet;
import org.smile.dataset.sort.Orderby;



public abstract class AbstractDataResultSet extends AbstractDataResult implements RowSet{
	/**
	 * 游标位置
	 */
	protected int cursor=-1;
	/**
	 * 标记位置
	 */
	private int mark;
	
	@Override
	public void toFirst() {
		this.cursor=0;
	}

	@Override
	public void skip(int i) {
		this.cursor+=i;
	}

	@Override
	public void remark() {
		this.mark=cursor;
	}

	@Override
	public void reset() {
		this.cursor=this.mark;
	}
	
	@Override
	public boolean rollNext() {
		if(++this.cursor>=this.length()){
			return false;
		}
		return true;
	}

	@Override
	public Row currentRow() {
		return rowAt(cursor);
	}
	
	@Override
	public IndexRowSet index(String[] field) {
		int[] cols=new int[field.length];
		for(int i=0;i<field.length;i++){
			cols[i]=getMetaData().getColumnIndex(field[i]);
		}
		return new IndexRowSet(this, cols);
	}
	
	@Override
	public CrossIndexRowSet crossIndex(String[] rowField,String[] columnField){
		int[] rowcols=new int[rowField.length];
		for(int i=0;i<rowField.length;i++){
			rowcols[i]=getMetaData().getColumnIndex(rowField[i]);
		}
		int[] colcols=new int[columnField.length];
		for(int i=0;i<columnField.length;i++){
			colcols[i]=getMetaData().getColumnIndex(columnField[i]);
		}
		return new CrossIndexRowSet(this, rowcols,colcols);
	}

	@Override
	public GroupRowSet group(String field) {
		Group group=new Group(field);
		return new GroupRowSet(this, group);
	}
	
	@Override
	public GroupRowSet group(String field,Orderby orderby) {
		return new GroupRowSet(this, new Group(field, orderby));
	}

}
