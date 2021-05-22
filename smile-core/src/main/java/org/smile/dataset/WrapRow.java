package org.smile.dataset;

import java.util.ArrayList;
import java.util.List;
/**
 * 对一行数据进行包装形成新的数据行
 * @author 胡真山
 *
 */
public class WrapRow extends AbstractRow{
	/**数据来源行*/
	private Row row;
	
	public WrapRow(DataSetMetaData metaData,Row row){
		this.metaData=metaData;
		this.row=row;
	}

	@Override
	public Object[] values(int[] columns) {
		Object[] result=new Object[columns.length];
		for(int i=0;i<columns.length;i++){
			result[i]=get(columns[i]);
		}
		return result;
	}

	@Override
	public Object[] toArray() {
		return toList().toArray();
	}

	@Override
	public Object value(int colmun) {
		String name=this.metaData.getColumnName(colmun);
		return row.get(name);
	}

	@Override
	public List<Object> toList() {
		List<Object> list=new ArrayList<Object>(this.metaData.getColumnCount());
		for(int i=0;i<this.metaData.getColumnCount();i++){
			list.add(get(i));
		}
		return list;
	}

	@Override
	public void set(int column, Object value) {
		String name=this.metaData.getColumnName(column);
		this.row.set(name, value);
	}

	@Override
	public Object set(String column, Object value) {
		return this.row.set(column, value);
	}


	@Override
	public Object get(String column) {
		return this.row.get(column);
	}

	@Override
	public Object get(int col) {
		String name=this.metaData.getColumnName(col);
		return this.row.get(name);
	}

	@Override
	public Row copy() {
		Row copy=this.row.copy();
		return new WrapRow(metaData, copy);
	}

}
