package org.smile.db.result;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.smile.beans.converter.BasicConverter;
import org.smile.db.result.type.BigDecimalColumnHandler;
import org.smile.db.result.type.BlobColumnHandler;
import org.smile.db.result.type.BooleanColumnHandler;
import org.smile.db.result.type.ByteArrayColumnHandler;
import org.smile.db.result.type.ByteColumnHandler;
import org.smile.db.result.type.CharColumnHandler;
import org.smile.db.result.type.ClobColumnHandler;
import org.smile.db.result.type.DateColumnHandler;
import org.smile.db.result.type.DoubleColumnHandler;
import org.smile.db.result.type.FloatColumnHandler;
import org.smile.db.result.type.IntegerColumnHandler;
import org.smile.db.result.type.LongColumnHandler;
import org.smile.db.result.type.ShortColumnHandler;
import org.smile.db.result.type.SqlDateColumnHandler;
import org.smile.db.result.type.StringColumnHandler;
import org.smile.db.result.type.TimeColumnHandler;
import org.smile.db.result.type.TimestampColumnHandler;

/**
 * 数据库列类型与java类型的转换
 * @author 胡真山
 * 2015年12月25日
 */
public class ColumnUtils {
	/**
	 * 类型与处理转换的对应
	 */
	private static Map<Class, ColumnHandler> handlers = new HashMap<Class, ColumnHandler>();
	/**
	 * 为空时的默认值
	 */
	private static Map<Class, Object> nullDefault = new HashMap<Class, Object>();

	static {
		handlers.put(Long.class, new LongColumnHandler());
		handlers.put(Short.class, new ShortColumnHandler());
		handlers.put(Integer.class, new IntegerColumnHandler());
		handlers.put(Byte.class, new ByteColumnHandler());
		handlers.put(Float.class, new FloatColumnHandler());
		handlers.put(Double.class, new DoubleColumnHandler());
		handlers.put(String.class, new StringColumnHandler());
		handlers.put(Character.class, new CharColumnHandler());
		handlers.put(Boolean.class, new BooleanColumnHandler());
		handlers.put(Date.class, new DateColumnHandler());
		handlers.put(java.sql.Date.class, new SqlDateColumnHandler());
		handlers.put(Time.class, new TimeColumnHandler());
		handlers.put(BigDecimal.class, new BigDecimalColumnHandler());
		handlers.put(Timestamp.class, new TimestampColumnHandler());
		handlers.put(Clob.class, new ClobColumnHandler());
		handlers.put(Blob.class, new BlobColumnHandler());

		handlers.put(long.class, new LongColumnHandler());
		handlers.put(short.class, new ShortColumnHandler());
		handlers.put(int.class, new IntegerColumnHandler());
		handlers.put(byte.class, new ByteColumnHandler());
		handlers.put(float.class, new FloatColumnHandler());
		handlers.put(double.class, new DoubleColumnHandler());
		handlers.put(char.class, new CharColumnHandler());
		handlers.put(boolean.class, new BooleanColumnHandler());
		handlers.put(byte[].class, new ByteArrayColumnHandler());
		
		
		
		nullDefault.put(Boolean.class, false);
		nullDefault.put(BigDecimal.class, new BigDecimal(0));
	}
	/**
	 * 注册一个目录类型处理器
	 * @param clazz 目标类型
	 * @param handler
	 */
	public static void registHandler(Class clazz,ColumnHandler handler){
		handlers.put(clazz, handler);
	}

	/**
	 * 从数据库结果集中获取一个列的值 
	 * @param javaType 转换为java的类型
	 * @param rs 结果集
	 * @param index 列的索引
	 * @return 
	 * @throws SQLException
	 */
	public static <T> T getColumn(Class<T> javaType, ResultSet rs, int index) throws SQLException {
		try {
			ColumnHandler<T> handler = handlers.get(javaType);
			if (handler != null) {
				return handler.getColumn(rs, index);
			} else {
				return (T) BasicConverter.getInstance().convert(javaType, rs.getObject(index));
			}
		} catch (Exception e) {
			throw new SQLException("Get column index " + index + " covert to " + javaType + " cause  error ", e);
		}
	}

	/**
	 * 从数据库结果集中获取一个列的值 
	 * @param javaType 转换为java的类型
	 * @param rs 结果集
	 * @param column 列的名称
	 * @return 
	 * @throws SQLException
	 */
	public static <T> T getColumn(Class<T> javaType, ResultSet rs, String column) throws SQLException {
		try {
			ColumnHandler<T> handler = handlers.get(javaType);
			if (handler != null) {
				return handler.getColumn(rs, column);
			} else {
				return (T) BasicConverter.getInstance().convert(javaType, rs.getObject(column));
			}
		} catch (Exception e) {
			throw new SQLException("Get column name " + column + " covert to " + javaType + " cause  error ", e);
		}
	}

	/**
	 * 设置{@link PreparedStatement} 的值
	 * @param ps  {@link PreparedStatement} 
	 * @param index
	 * @param value 
	 * @throws SQLException
	 */
	public static void setColumn(PreparedStatement ps, int index, Object value) throws SQLException {
		if (value == null) {
			ps.setNull(index, Types.VARCHAR);
		} else {
			ColumnHandler handler = handlers.get(value.getClass());
			if (handler != null) {
				handler.setColumn(ps, index, value);
			} else {
				ps.setObject(index, value);
			}
		}
	}

	/**
	 * 设置一个空值
	 * @param ps
	 * @param index
	 * @throws SQLException
	 */
	public static void setNull(PreparedStatement ps, int index) throws SQLException {
		ps.setNull(index, Types.VARCHAR);
	}

	/**
	 * 设置一个空值
	 * @param ps
	 * @param index
	 * @throws SQLException
	 */
	public static void setNull(PreparedStatement ps, Class type, int index) throws SQLException {
		Object obj = nullDefault.get(type);
		if (obj != null) {
			ps.setObject(index, obj);
		} else {
			ps.setNull(index, Types.VARCHAR);
		}
	}

}
