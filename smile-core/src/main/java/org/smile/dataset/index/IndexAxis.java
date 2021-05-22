package org.smile.dataset.index;

import org.smile.collection.ThreadLocalMap;
import org.smile.dataset.Key;
import org.smile.dataset.Row;

/**
 * 键的转轴
 * @author 胡真山
 *
 */
public class IndexAxis {
	/**
	 * 行索引键
	 */
	private static final String INEX_KEY="index_key";
	/***
	 * 列索引键
	 */
	private static final String INDEX_COLUMN_KEY="index_column_key";
	/**
	 * 当前行数据
	 */
	private static final String CURRENT_ROW="current_row";
	
	protected static ThreadLocalMap<String,Object> axis=new ThreadLocalMap<String,Object>();
	/**
	 * 设置当前行索引
	 * @param key
	 */
	public static void indexKey(Key key){
		axis.put(INEX_KEY, key);
	}
	/**
	 * 获取当前索引键
	 * @return
	 */
	public static Key indexKey(){
		return (Key) axis.get(INEX_KEY);
	}
	/**
	 * 设置列索引键
	 * @param key
	 */
	public static void indexColumnKey(Key key){
		axis.put(INDEX_COLUMN_KEY, key);
	}
	/**
	 * 当前列索引的键
	 * @return
	 */
	public static Key indexColumnKey(){
		return (Key) axis.get(INDEX_COLUMN_KEY);
	}
	
	/**
	 * 设置当前行数据
	 * @param row
	 */
	public static void currentRow(Row row){
		axis.put(CURRENT_ROW, row);
	}
	/**
	 * 当前迭代到的行
	 * @return
	 */
	public static Row currentRow(){
		return (Row) axis.get(CURRENT_ROW);
	}
}
