package org.smile.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.commons.Strings;
import org.smile.io.buff.BufferException;
import org.smile.json.JSON;
import org.smile.util.XmlUtils;

/**
 * 数据读取  可从byte[] 中读取基础数据类型
 * @author 胡真山
 * 2015年10月23日
 */
public class BaseDataReader implements DataReader {

	protected DataInputStream dis;

	public BaseDataReader() {
	}

	/**
	 * 由inputStream构建
	 * @param is
	 */
	public BaseDataReader(InputStream is) {
		dis = new DataInputStream(is);
	}

	/**
	 * 由DataInputStream 构建
	 * @param is
	 */
	public BaseDataReader(DataInputStream is) {
		this.dis = is;
	}

	/**
	 * 重新设置要读取的内容
	 * @param bytes 
	 */
	public void setReadBytes(byte[] bytes) {
		dis = new DataInputStream(new ByteArrayInputStream(bytes));
	}

	/**
	 * 由byte数组构建
	 * @param data
	 */
	public BaseDataReader(byte[] data) {
		dis = new DataInputStream(new ByteArrayInputStream(data));
	}

	public BaseDataReader(byte[] data, int i, int j) {
		dis = new DataInputStream(new ByteArrayInputStream(data, i, j));
	}

	public <T> T readJSON(Class<T> clazz) throws IOException {
		try {
			String str = dis.readUTF();
			Object value = JSON.parse(str);
			if (clazz.isAssignableFrom(value.getClass())) {
				return clazz.cast(value);
			}
			return BasicConverter.getInstance().convert(clazz, value);
		} catch (Exception e) {
			throw new IOException("readjson " + clazz, e);
		}
	}

	public Map readJSON() throws IOException {
		return readJSON(Map.class);
	}

	/**
	 * 从开始重新读取
	 */
	public void reset() {
		try {
			dis.reset();
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}

	@Override
	public int readInteger() throws IOException {
		return dis.readInt();
	}

	@Override
	public byte readByte() throws IOException {
		return dis.readByte();
	}

	@Override
	public short readShort() throws IOException {
		return dis.readShort();
	}

	@Override
	public long readLong() throws IOException {
		return dis.readLong();
	}

	@Override
	public float readFloat() throws IOException {
		return dis.readFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return dis.readDouble();
	}

	@Override
	public boolean readBoolean() throws IOException {
		return dis.readBoolean();
	}

	@Override
	public String readString() throws IOException {
		return dis.readUTF();
	}

	@Override
	public byte[] readByteArray() throws IOException {
		int len = dis.readInt();
		byte[] b = new byte[len];
		dis.readFully(b);
		return b;
	}

	@Override
	public byte[] readUTFBytes() throws IOException {
		String str = dis.readUTF();
		return str.getBytes(IOUtils.DEFAULT_ENCODE);
	}

	@Override
	public Object readBean() throws IOException, ClassNotFoundException {
		String name = dis.readUTF();
		Class clazz = Class.forName(name);
		String str = dis.readUTF();
		Object value = JSON.parse(str);
		if (clazz.isAssignableFrom(value.getClass())) {
			clazz.cast(value);
		}
		try {
			return BasicConverter.getInstance().convert(clazz, value);
		} catch (ConvertException e) {
			throw new IOException("转换成" + name + "失败", e);
		}
	}

	@Override
	public String readUTFString() throws IOException {
		int len = dis.readUnsignedShort();
		byte[] bytes = new byte[len];
		dis.readFully(bytes);
		return new String(bytes, Strings.UTF_8);
	}

	@Override
	public Object readXmlObject() throws IOException, ClassNotFoundException {
		String name = dis.readUTF();
		Class clazz = Class.forName(name);
		String xml = dis.readUTF();
		return XmlUtils.parserXml(clazz, xml);
	}

	@Override
	public char readChar() throws IOException {
		return dis.readChar();
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		dis.readFully(b);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		dis.readFully(b, off, len);
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return dis.skipBytes(n);
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return dis.readUnsignedByte();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return dis.readUnsignedShort();
	}

	@Override
	public int readInt() throws IOException {
		return dis.readInt();
	}

	@Override
	public String readLine() throws IOException {
		return dis.readLine();
	}

	@Override
	public String readUTF() throws IOException {
		return dis.readUTF();
	}

	@Override
	public int readabled() throws IOException {
		return dis.available();
	}

}
