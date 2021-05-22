package org.smile.dataset;

import java.util.Map;
import java.util.Set;

import org.smile.collection.LinkedHashMap;
import org.smile.commons.SmileRunException;

public class DataSetMetaDataImpl implements DataSetMetaData {
	
	Map<String,Integer> namedIndexs=new LinkedHashMap<String,Integer>();
	
	Map<Integer,String> columns=new LinkedHashMap<Integer,String>();
	
	Map<Integer,Class> columnTypes=new LinkedHashMap<Integer,Class>();
	
	public DataSetMetaDataImpl(String[] names){
		for(int i=0;i<names.length;i++){
			String name=names[i];
			if(namedIndexs.containsKey(name)){
				throw new SmileRunException("duplicate column name "+name);
			}
			namedIndexs.put(name, i);
			columns.put(i, names[i]);
		}
	}
	
	@Override
	public String getColumnName(int col) {
		return columns.get(col);
	}

	@Override
	public Class getColumType(int col) {
		return columnTypes.get(col);
	}

	@Override
	public int getColumnIndex(String name) {
		Integer index= namedIndexs.get(name);
		return index==null?-1:index;
	}

	@Override
	public Class getColumType(String name) {
		return columnTypes.get(namedIndexs.get(name));
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public Set<String> columnNames() {
		return namedIndexs.keySet();
	}

	@Override
	public String toString() {
		return columns.toString();
	}

	@Override
	public void setColumnType(int col, Class type) {
		this.columnTypes.put(col, type);
	}
	
}
