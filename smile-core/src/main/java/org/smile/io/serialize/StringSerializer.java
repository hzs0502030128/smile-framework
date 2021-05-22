package org.smile.io.serialize;

import java.io.UnsupportedEncodingException;
/**
 * 对字符串进行序列化
 * @author 胡真山
 *
 */
public class StringSerializer implements Serializer<String>{
	/**字符串编码规则*/
	protected String charset="UTF-8";
	
	@Override
	public String deserialize(byte[] bytes) throws SerializeException {
		try {
			return (bytes == null ? null : new String(bytes, charset));
		} catch (UnsupportedEncodingException e) {
			throw new SerializeException("convert to string "+charset, e);
		}
	}

	@Override
	public byte[] serialize(String string) throws SerializeException {
		try {
			return  (string == null ? null : string.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			throw new SerializeException("serialize string "+charset, e);
		}
	}

	@Override
	public void setLoader(ClassLoader loader) {
		
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
}
