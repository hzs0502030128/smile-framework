package org.smile.dataset;


public class RangeRowSet extends AbstractRowSet{
	/**区间的开始*/
	private int start;
	/**区间结束*/
	private int end;
	
	public RangeRowSet(DataSet dataSet,int start,int end){
		this.dataSet=dataSet;
		this.start=start;
		this.end=end;
	}
	
	@Override
	public int firstIndex() {
		return start;
	}
	
	@Override
	public int lastIndex() {
		return end;
	}
	
	@Override
	public Object[] valuesAt(int col) {
		Object[] cols=new Object[length()];
		for(int i=start;i<=end;i++){
			cols[i]=dataSet.rowAt(i).get(col);
		}
		return cols;
	}
	
	@Override
	public Object valueAt(int col) {
		Row row=dataSet.rowAt(start+cursor);
		return row.get(col);
	}
	
	@Override
	public Row[] toArray() {
		Row[] rows=new Row[length()];
		int j=0;
		for(int i=start;i<=end;i++){
			rows[j++]=dataSet.rowAt(i);
		}
		return rows;
	}
	@Override
	public int rowIndex(int i) {
		return start+i;
	}
	
	@Override
	public int length() {
		return end-start+1;
	}
	
	@Override
	public boolean hasNext() {
		return cursor<end;
	}

	@Override
	public Row next() {
		cursor++;
		return dataSet.rowAt(start+cursor);
	}
	
}
