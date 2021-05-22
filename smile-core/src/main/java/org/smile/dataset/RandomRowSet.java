package org.smile.dataset;


public class RandomRowSet extends AbstractRowSet{
	/**此行集全所在数据集的行索引*/
	int[] rowIndexs;
	
	public RandomRowSet(DataSet dataSet,int[] rows){
		this.dataSet=dataSet;
		this.rowIndexs=rows;
	}
	
	@Override
	public int firstIndex() {
		return rowIndexs[0];
	}
	
	@Override
	public int lastIndex() {
		return rowIndexs[rowIndexs.length-1];
	}
	
	@Override
	public Object[] valuesAt(int col) {
		Object[] cols=new Object[length()];
		for(int i=0;i<rowIndexs.length;i++){
			cols[i]=dataSet.rowAt(rowIndexs[i]).get(col);
		}
		return cols;
	}
	
	@Override
	public Object valueAt(int col) {
		Row row=dataSet.rowAt(this.cursor);
		return row.get(col);
	}
	
	@Override
	public Row[] toArray() {
		Row[] rows=new Row[length()];
		for(int i=0;i<rowIndexs.length;i++){
			rows[i]=dataSet.rowAt(rowIndexs[i]);
		}
		return rows;
	}
	@Override
	public int rowIndex(int i) {
		return rowIndexs[i];
	}
	
	@Override
	public int length() {
		return rowIndexs.length;
	}
	
	@Override
	public boolean hasNext() {
		return cursor<rowIndexs.length-1;
	}
	
}
