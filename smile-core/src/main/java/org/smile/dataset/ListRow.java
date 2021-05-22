package org.smile.dataset;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author 胡真山
 *
 */
public class ListRow extends AbstractRow{
	/**
	 * 行的数据内容 以列表形式保存
	 */
	protected List<Object> dataList; 
	/**
	 * 构造一个行数据
	 * @param metaData 元素据定义
	 * @param dataList 行数据内容
	 */
	public ListRow(DataSetMetaData metaData,List<Object> dataList){
		this.metaData=metaData;
		this.dataList=dataList;
	}
	
	@Override
	public Object get(int col) {
		return dataList.get(col);
	}

	@Override
	public Object get(String column) {
		int i=this.metaData.getColumnIndex(column);
		return dataList.get(i);
	}

	@Override
	public Object[] toArray() {
		return dataList.toArray();
	}


	@Override
	public Object[] values(int[] columns) {
		Object[] result=new Object[columns.length];
		for(int i=0;i<columns.length;i++){
			result[i]=dataList.get(columns[i]);
		}
		return result;
	}

	@Override
	public Object value(int colmun) {
		return dataList.get(colmun);
	}

	@Override
	public List<Object> toList() {
		return new ArrayList<Object>(dataList);
	}

	@Override
	public void set(int column, Object value) {
		this.dataList.set(column, value);
	}

	@Override
	public Object set(String column, Object value) {
		int i=this.metaData.getColumnIndex(column);
		return this.dataList.get(i);
	}

	@Override
	public Row copy() {
		ArrayList<Object> copyData=new ArrayList<Object>(this.dataList);
		Row row=new ListRow(metaData, copyData);
		return row;
	}
}
