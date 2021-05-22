package org.smile.dataset;

import java.util.Arrays;
import java.util.List;

public class ArrayRow extends AbstractRow{
	/**行的数据以数组形式保存*/
	protected Object[] dataArray; 
	/**
	 * 构造一个行数据
	 * @param metaData 元素据定义
	 * @param dataArray 行数内容
	 */
	public ArrayRow(DataSetMetaData metaData,Object[] dataArray){
		this.metaData=metaData;
		this.dataArray=dataArray;
	}
	
	@Override
	public Object get(int col) {
		return dataArray[col];
	}

	@Override
	public Object get(String column) {
		int colIdx=metaData.getColumnIndex(column);
		return dataArray[colIdx];
	}

	@Override
	public Object[] values(int[] columns) {
		Object[] result=new Object[columns.length];
		for(int i=0;i<columns.length;i++){
			result[i]=dataArray[columns[i]];
		}
		return result;
	}

	@Override
	public Object[] toArray() {
		Object[] copy=new Object[dataArray.length];
		System.arraycopy(dataArray, 0, copy, 0, dataArray.length);
		return copy;
	}

	@Override
	public Object value(int colmun) {
		return dataArray[colmun];
	}

	@Override
	public List<Object> toList() {
		return Arrays.asList(dataArray);
	}

	@Override
	public void set(int column, Object value) {
		this.dataArray[column]=value;
	}

	@Override
	public Object set(String column, Object value) {
		int idx=metaData.getColumnIndex(column);
		Object remove=this.dataArray[idx];
		this.dataArray[idx]=value;
		return remove;
	}

	@Override
	public String toString() {
		if(dataArray==null){
			return "[]";
		}
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for(int i=0;i<dataArray.length;i++){
			if(i>0){
				sb.append(",");
			}
			sb.append(dataArray[i]);
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public Row copy() {
		Object[] copyData=new Object[dataArray.length];
		System.arraycopy(dataArray, 0, copyData, 0, copyData.length);
		Row row=new ArrayRow(metaData, copyData);
		return row;
	}
	
}
