package org.smile.dataset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseDataSet extends AbstractDataResultSet implements DataSet {

	/**数据集元数据*/
	protected DataSetMetaData metaData;
	/**数据集的行数据*/
	protected ArrayList<Row> rows;
	
	public BaseDataSet(DataSetMetaData metaDate){
		this.metaData=metaDate;
		this.rows=new ArrayList<Row>();
	}
	
	public BaseDataSet(DataSetMetaData metaDate,List<Row> rows){
		this.metaData=metaDate;
		this.rows=new ArrayList<Row>(rows);
		for(int i=0;i<rows.size();i++){
			rows.get(i).setIndex(i);
		}
	}

	@Override
	public Row[] toArray() {
		return rows.toArray(new Row[rows.size()]);
	}

	@Override
	public int length() {
		return rows.size();
	}

	@Override
	public Row rowAt(int i) {
		return rows.get(i);
	}

	@Override
	public Object[] valuesAt(int col) {
		int size = rows.size();
		Object[] cols = new Object[size];
		for (int i = 0; i < size; i++) {
			cols[i] = rows.get(i).get(col);
		}
		return cols;
	}

	@Override
	public DataSetMetaData getMetaData() {
		return metaData;
	}

	@Override
	public Object get(int col) {
		return rows.get(cursor).get(col);
	}

	@Override
	public Object get(String column) {
		int col = metaData.getColumnIndex(column);
		return rows.get(cursor).get(col);
	}

	@Override
	public Object valueAt(int col) {
		return get(col);
	}

	@Override
	public boolean hasNext() {
		return this.cursor < this.rows.size() - 1;
	}

	@Override
	public Row next() {
		cursor++;
		return rows.get(cursor);
	}

	@Override
	public void remove() {
		this.rows.remove(cursor);
	}

	@Override
	public Iterator<Row> iterator() {
		return this;
	}

	@Override
	public RowSet rangeRowSet(int start, int end) {
		return new RangeRowSet(this,start,end);
	}

	@Override
	public RowSet randomRowSet(int[] rows) {
		return new RandomRowSet(this,rows);
	}

	@Override
	public void addRow(Row row) {
		row.setIndex(rows.size());
		this.rows.add(row);
	}

	@Override
	public int getColumnIndex(String columnName) {
		return metaData.getColumnIndex(columnName);
	}

	@Override
	public String getColumnName(int column) {
		return metaData.getColumnName(column);
	}

	@Override
	public int getColumnCount() {
		return metaData.getColumnCount();
	}

	@Override
	public int firstIndex() {
		return 0;
	}

	@Override
	public int lastIndex() {
		return this.rows.size()-1;
	}

	@Override
	public int rowIndex(int i) {
		return i;
	}

	@Override
	public DataSet getDataSet() {
		return this;
	}

	@Override
	public int rowCount() {
		return this.rows.size();
	}

}
