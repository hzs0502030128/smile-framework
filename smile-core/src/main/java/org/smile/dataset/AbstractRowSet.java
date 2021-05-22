package org.smile.dataset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.smile.beans.BeanProperties;
import org.smile.beans.PropertiesGetter;
import org.smile.beans.converter.BeanException;
import org.smile.commons.SmileRunException;
import org.smile.dataset.field.BaseField;


public abstract class AbstractRowSet extends AbstractDataResultSet implements RowSet , PropertiesGetter<String, Object>{
	/***
	 * 源数据集
	 */
	protected DataSet dataSet;
	
	protected Key key;
	
	protected Map<String,BaseField> filedCache=new HashMap<String, BaseField>();
	
	@Override
	public void remove() {
		throw new IllegalArgumentException("not support this method");
	}
	
	@Override
	public Row rowAt(int i) {
		return dataSet.rowAt(rowIndex(i));
	}
	
	@Override
	public Object get(int col) {
		return dataSet.rowAt(rowIndex(cursor)).get(col);
	}
	
	@Override
	public Object get(String column) {
		return dataSet.rowAt(rowIndex(cursor)).get(column);
	}
	
	@Override
	public Iterator<Row> iterator() {
		return this;
	}

	@Override
	public DataSetMetaData getMetaData() {
		return dataSet.getMetaData();
	}
	
	@Override
	public Row next() {
		cursor++;
		Row r=dataSet.rowAt(rowIndex(cursor));
		return r;
	}

	@Override
	public int getColumnIndex(String columnName) {
		return dataSet.getMetaData().getColumnIndex(columnName);
	}
	
	@Override
	public String getColumnName(int column) {
		return dataSet.getMetaData().getColumnName(column);
	}
	
	@Override
	public int getColumnCount(){
		return dataSet.getMetaData().getColumnCount();
	}
	
	@Override
	public DataSet getDataSet(){
		return dataSet;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
	
	/**
	 * 生成列字段
	 * @param fieldName
	 * @return
	 */
	public BaseField field(String fieldName) {
		BaseField field=filedCache.get(fieldName);
		if(field==null){
			int col=this.dataSet.getColumnIndex(fieldName);
			field=new BaseField(this, col);
			filedCache.put(fieldName, field);
		}
		return field;
	}

	@Override
	public Object getValue(String name) {
		if(dataSet.getColumnIndex(name)>0){
			return field(name);
		}
		try {
			return BeanProperties.NORAL_CAN_NO_PROPERTY.getFieldValue(this, name);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
}
