package org.smile.db.result;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.Converter;
import org.smile.dataset.ArrayRow;
import org.smile.dataset.BaseDataSet;
import org.smile.dataset.DataSet;
import org.smile.dataset.DataSetMetaData;
import org.smile.dataset.DataSetMetaDataImpl;
import org.smile.db.handler.MapRowHandler;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.handler.RowHandler;
import org.smile.util.DateUtils;
import org.smile.util.StringUtils;
/**
 * 数据库 结果结果集操作
 * @author 胡真山
 * 2015年11月24日
 */
public class ResultUtils {
	/**列转换*/
	protected static ColumnParser columnParser=new BaseColumnParser();
	
	public static Converter CONVERTER=BasicConverter.getInstance();
	/**
	 * 获取列的值
	 * @param rs
	 * @param key
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public static Object getColumn(ResultSet rs,String key,Class<?> type) throws SQLException{
		return columnParser.parseResultColumn(rs, key,type);
	}
	/**
	 * 列的值转换为目标类型后的值
	 * @param rs 
	 * @param index 列的索引
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public static Object getColumn(ResultSet rs,int index,Class<?> type) throws SQLException{
		if(type==String.class){
			return getString(rs, index);
		}
		Object value=rs.getObject(index);
		return convertColumn(value, type,"index "+index);
	}
	/**
	 * 转换列类型
	 * @param value 源值
	 * @param type 目标类型
	 * @param error 提示语
	 * @return 转换为目标类型后的值
	 * @throws SQLException
	 */
	protected static Object convertColumn(Object value,Class<?> type,String error) throws SQLException{
		if ( !type.isPrimitive() &&  value== null ) {
	         return null;
	    }else if(type==String.class){
			return convertToString(value);
		}
		try {
			return CONVERTER.convert(type, value);
		} catch (ConvertException e) {
			throw new SQLException("result convert error flag:"+error,e);
		}

	}
	/***
	 * 转为string 类型
	 * @param rs
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	public static String getString(ResultSet rs,String key) throws SQLException{
		Object value=rs.getObject(key);
		return convertToString(value);
	}
	/**
	 * 获取String字段
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public static String getString(ResultSet rs,int index) throws SQLException{
		int type=rs.getMetaData().getColumnType(index);
		Object value=rs.getObject(index);
		if(type==java.sql.Types.CHAR){
			return StringUtils.trim((String)value);
		}
		return convertToString(value);
	}
	/***
	 * 获取自动生成的key值
	 * @param ps
	 * @return
	 * @throws SQLException
	 */
	public static Object getGeneratedKey(PreparedStatement ps) throws SQLException{
		ResultSet rs=ps.getGeneratedKeys();
		if(rs.next()){
			return rs.getObject(1);
		}
		return null;
	}
	/***
	 * 填充生成的key到一个列表中
	 * @param ps
	 * @param keylist
	 * @throws SQLException
	 */
	public static void fillGeneratedKeyList(PreparedStatement ps,List keylist) throws SQLException{
		ResultSet rs=ps.getGeneratedKeys();
		while(rs.next()){
			keylist.add(rs.getObject(1));
		}
	}
	
	/**
	 * 以string类型返回
	 * @param rs
	 * @param index 
	 * @return
	 * @throws SQLException
	 */
	public static String getString(QueryResult rs,int index) throws SQLException{
		int type=rs.getColumnType(index);
		Object value=rs.getObject(index);
		if(type==java.sql.Types.CHAR){
			return StringUtils.trim((String)value);
		}
		return convertToString(value);
	}
	/***
	 * sql 结果 转为string 类型
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public static String convertToString(Object value) throws SQLException{
		if(value==null){
    		return null;
    	}else if(value instanceof String){
    		return  (String)value;
    	}else if(value instanceof Date){
    		return DateUtils.defaultFormat((Date)value);
    	}else if(value instanceof Clob){
    		Clob clob=(Clob)value;
    		int len=(int)clob.length();
    		String str=clob.getSubString(1, len);
    		return str;
    	}else if(value instanceof Blob){
    		Blob blob =(Blob)value;
    		return new String(blob.getBytes(1, (int)blob.length()));
    	}else if(value instanceof byte[]){
    		return new String((byte[])value);
    	}else if(value instanceof Byte[]){
    		Byte[] bt=(Byte[])value;
    		byte[] b=new byte[bt.length];
    		for(int i=0;i<b.length;i++){
    			b[i]=bt[i].byteValue();
    		}
    		return new String(b);
    	}else{
    		return String.valueOf(value);
    	}
	}
	/**
	 * 转换成map集合
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static List<ResultSetMap> parseToMap(ResultSet rs) throws SQLException{
		List<ResultSetMap> list=new LinkedList<ResultSetMap>();
    	RowHandler handler=MapRowHandler.RESULT_SET_MAP;
    	QueryResult result=new QueryResult(rs);
        while (rs.next()) {
        	ResultSetMap bean=handler.handle(result);
            list.add(bean);
        }
        return list;
	}
	/**
	 * 结果集转换成dataset
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static DataSet parseToDataSet(ResultSet rs) throws SQLException {
		QueryResult result = new QueryResult(rs);
		DataSetMetaData metaData=new DataSetMetaDataImpl(result.getColumnsNames().toArray(new String[0]));
		DataSet dataSet=new BaseDataSet(metaData);
		while (rs.next()) {
			Object[] bean =RowHandler.ARRAY.handle(result);
			dataSet.addRow(new ArrayRow(metaData,bean));
		}
		return dataSet;
	}
	
	public static final void setColumnParser(ColumnParser parser){
		columnParser=parser;
	}
	
	public static boolean jsonStore(Class fieldType){
		return columnParser.jsonStore(fieldType);
	}
	
	public static Object parseResultColumn(ResultSet rs,DatabaseColumn column) throws SQLException{
		return columnParser.parseResultColumn(rs,column);
	}
	
}
