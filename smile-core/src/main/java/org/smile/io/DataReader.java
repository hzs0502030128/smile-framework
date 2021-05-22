package org.smile.io;

import java.io.DataInput;
import java.io.IOException;

/**
 * 可以读取数据的接口
 * @author 胡真山
 *
 */
public interface DataReader extends DataInput{

	/**
	 * 读取一个int类型
	 * @return
	 */
	public  int readInteger()  throws IOException ;

	/**
	 * 读取一个byte
	 * @return
	 */
	public  byte readByte()  throws IOException ;

	/**
	 * 读取一个短整型
	 * @return
	 */
	public  short readShort()  throws IOException ;
	/**
	 * 读取一个长整型
	 * @return
	 */
	public  long readLong()  throws IOException ;

	/**
	 * 读取一个float型值
	 * @return
	 */
	public  float readFloat()  throws IOException ;

	/**
	 * 读取一个double型值
	 * @return
	 */
	public  double readDouble()  throws IOException ;

	/**
	 * 读取一个bool型值
	 * @return
	 */
	public  boolean readBoolean()  throws IOException ;

	/**
	 * 读取一个utf8格式的字符串
	 * @return
	 */
	public  String readString()  throws IOException ;
	/**
	 * 读取一个byte数组  
	 * 先读取一个int 长度 len
	 * 再读取一个长度为 len 的byte数组
	 * @return
	 */
	public  byte[] readByteArray()  throws IOException ;

	/**
	 * 以字符串的方式读取
	 * 读取后转为数组
	 * @return
	 */
	public  byte[] readUTFBytes()  throws IOException ;
	/**
	 * 以bytes形式读取字符串
	 *  格式  ：先读取一个int 的长度 l 
	 *  	再读取一个长度l的byte数组   
	 *  	byte数组转成字符串 以utf8的格式
	 * @return
	 * @throws IOException
	 */
	public String readUTFString() throws IOException;
	/**
	 * 读取一个对象
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object readBean() throws IOException, ClassNotFoundException;
	/**
	 * 读取一个以xml转送的对象
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object readXmlObject() throws IOException, ClassNotFoundException;
	/**
	 * 读取一个char
	 * @return
	 * @throws IOException
	 */
	public char readChar()  throws IOException;
	/**
	 * 可读取长度
	 * @return
	 * @throws IOException
	 */
	public int readabled() throws IOException;
}