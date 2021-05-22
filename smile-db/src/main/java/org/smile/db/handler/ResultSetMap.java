package org.smile.db.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import org.smile.Smile;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.type.BigDecimalConverter;
import org.smile.beans.converter.type.IntegerConverter;
import org.smile.beans.converter.type.LongConverter;
import org.smile.collection.KeyNoCaseHashMap;
import org.smile.collection.ResultMap;
import org.smile.commons.SmileException;
import org.smile.commons.SmileRunException;
import org.smile.db.SqlRunException;
import org.smile.db.result.DbResult;
import org.smile.db.result.ResultUtils;
import org.smile.io.IOUtils;

/**
 * 这个map 的值是不区分大小写的
 * 他的key只能是String类型的
 * @author strive
 *
 */
public class ResultSetMap extends KeyNoCaseHashMap<Object> implements DbResult,KeyColumnSwaper,ResultMap {
	/**** 列键转换器 */
	protected KeyColumnSwaper keyColumnSwaper=NoneKeyColumnSwaper.instance;
	/**
	 * byte数组形式存入
	 * @param key
	 * @param value 可以是输入流、字符串
	 * @throws SmileException 
	 * @throws IOException
	 */
	public void putBytes(String key, Object value) throws ConvertException {
		if (value instanceof byte[]) {
			this.put(key, value);
		} else if (value instanceof String) {
			this.put(key, ((String) value).getBytes());
		} else {
			throw new ConvertException("未预知的转换对象：" + value.getClass().getName() + " to byte[]");
		}
	}

	/**
	 * 把一个流以byte数据形式存入数据库
	 */
	public void putBytes(String key, InputStream is) throws IOException {
		this.put(key, IOUtils.stream2byte(is));
	}

	/**
	 * 以btye[] 返回
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	@Override
	public byte[] getBytes(String key) {
		Object value = get(key);
		try {
			if (value == null) {
				return null;
			} else if (value instanceof String) {
				return ((String) value).getBytes(Smile.ENCODE);
			} else if (value instanceof Blob) {
				Blob blob = (Blob) value;
				return blob.getBytes(1, (int) blob.length());
			} else if (value instanceof Clob) {
				Clob clob = (Clob) value;
				int len = (int) clob.length();
				String str = clob.getSubString(1, len);
				return str.getBytes(Smile.ENCODE);
			} else if (value instanceof byte[]) {
				return (byte[]) value;
			} else if (value instanceof Byte[]) {
				Byte[] bt = (Byte[]) value;
				byte[] b = new byte[bt.length];
				for (int i = 0; i < b.length; i++) {
					b[i] = bt[i].byteValue();
				}
				return b;
			}
		}catch(SQLException e) {
			throw new SqlRunException(e);
		} catch (UnsupportedEncodingException e) {
			throw new SmileRunException(e);
		}
		throw new SqlRunException("不支持的转换类型:"+value.getClass());
	}

	@Override
	public Integer getInt(String key) {
		Object value = get(key);
		try {
			return IntegerConverter.instance.convert(value);
		} catch (ConvertException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public Long getLong(String key) {
		Object value = get(key);
		try {
			return LongConverter.instance.convert(value);
		} catch (ConvertException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		Object value = get(key);
		try {
			return BigDecimalConverter.instance.convert(value);
		} catch (ConvertException e) {
			throw new SmileRunException(e);
		}
	}

	/**
	 * 以String方式返回
	 * @param key
	 * @return
	 */
	@Override
	public String getString(String key) {
		Object value = get(key);
		try {
			return ResultUtils.convertToString(value);
		} catch (SQLException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public Object getObject(String column) {
		return get(column);
	}

	@Override
	public String columnToKey(String column) {
		return this.keyColumnSwaper.columnToKey(column);
	}

	@Override
	public String KeyToColumn(String key) {
		return this.keyColumnSwaper.KeyToColumn(key);
	}

	public void setKeyColumnSwaper(KeyColumnSwaper keyColumnSwaper) {
		this.keyColumnSwaper = keyColumnSwaper;
	}
	
	
}
