package org.smile.io;

import java.io.DataOutput;
import java.io.IOException;

public interface DataWriter extends DataOutput{
	/**
	 * 设置一个字节
	 * @param value
	 * @throws IOException
	 */
    public void writeByte(int b) throws IOException ;
    /**
     * 设置一个布尔型
     * @param value
     * @throws IOException
     */
	public void writeBoolean(boolean bool) throws IOException;
	/**
	 * 只是写入数组
	 * @param value
	 * @throws IOException
	 */
	public void writeBytes(byte[] bytes) throws IOException ;
	/**
	 * 写入一个数组   
	 * 采购的方法是先写入一个数组的长度 int 型
	 * 再写入数组内容
	 * @param bytes
	 * @throws IOException
	 */
	public void writeByteArray(byte[] bytes) throws IOException;
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void writeShort(int s) throws IOException ;
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void writeInteger(int i) throws IOException ;
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void writeLong(long l) throws IOException ;
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void writeFloat(float f) throws IOException ;
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void writeDouble(double d) throws IOException ;
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void writeString(String str) throws IOException ;
	/**
	 * 以字符串的形式写入byte数组
	 * @param bytes
	 * @throws IOException
	 */
	public void writeUTFBytes(byte[] bytes) throws IOException;
	/**
	 * @param obj
	 * @throws IOException
	 */
	public void writeBean(Object obj) throws IOException;
	/**
	 * 转成数组
	 * @return
	 * @throws IOException
	 */
	public byte[] toByteArray() throws IOException;
	/**
	 * 以byte的形式写入字符串
	 *   格式为：先写一个字符串的bytes长度
	 *   	  再写入字符串的bytes数组以utf8格式转成bytes
	 * @throws IOException
	 */
	public void writeUTFString(String str) throws IOException;
	/**
	 * 写入一个以xml转送的对象
	 * @param obj
	 * @throws IOException
	 */
	public void writeXmlObject(Object obj) throws IOException;
	/**
	 * 写入一个字符型
	 * @param chr
	 * @throws IOException
	 */
	public void writeChar(char chr) throws IOException;
}
