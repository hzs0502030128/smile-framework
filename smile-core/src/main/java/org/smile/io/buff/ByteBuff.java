package org.smile.io.buff;

import java.io.IOException;

import org.smile.io.DataReader;
import org.smile.io.DataWriter;
import org.smile.io.Input;
import org.smile.io.Output;

/**
 * 写入数据的时候是没有参是否可写入多少数据进行验证，
 * 所以在写入数据前需自行判断 读取数据时没有对可读取的长度进行判断，所以在读取数据的时候需行判断
 * 
 * @author 胡真山
 * @see java.io.DataInputStream
 * @see java.io.DataOutputStream
 */
public interface ByteBuff extends DataWriter, DataReader, Input, Output {
	/**
	 * 标记读取索引
	 */
	public void mark() throws IOException;

	/**
	 * 重置缓存读取信息
	 */
	public void reset() throws IOException;

	/**
	 * 读取一个字节
	 * 
	 * @return
	 */
	public int read() throws IOException;

	/**
	 * 跳过
	 * 
	 * @param n
	 * @return
	 */
	public long skip(long n) throws IOException;

	/**
	 * 是否有可读取数据
	 * 
	 * @return
	 */
	public int readabled() throws IOException;

	/**
	 * 是否可写数据
	 * 
	 * @return
	 */
	public int writeabled() throws IOException;
	/**
	 * 读取一个
	 * @param b
	 * @return
	 * @throws IOException
	 */
	public int read(byte[] b) throws IOException;

	/**
	 * 转成bytearray
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte[] toByteArray() throws IOException;

	/**
	 * 清空缓存空间
	 * @throws IOException
	 */
	public void clear() throws IOException;

}
