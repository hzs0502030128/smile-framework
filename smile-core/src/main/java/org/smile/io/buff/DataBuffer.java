package org.smile.io.buff;

import java.io.IOException;

import org.smile.io.BaseDataWriter;
import org.smile.io.DataWriter;
import org.smile.json.JSON;
/**
 * 一个用于缓存数据的buffer 
 * 支持写入、读取 基础数据类型
 * @author 胡真山
 * 2015年10月23日
 */
public class DataBuffer{
	
	protected DataWriter writer=new BaseDataWriter();
	
	public DataBuffer appendInt(int v){
		try {
			writer.writeInteger(v);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}
	
	public DataBuffer appendShort(int v){
		try {
			writer.writeShort(v);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}
	
	public DataBuffer appendLong(long v){
		try {
			writer.writeLong(v);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}
	
	public DataBuffer appendByte(byte v){
		try {
			writer.writeByte(v);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}
	
	public DataBuffer appendFloat(float v){
		try {
			writer.writeFloat(v);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}
	
	public DataBuffer appendDouble(double v){
		try {
			writer.writeDouble(v);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}
	
	public DataBuffer appendChar(char v){
		try {
			writer.writeChar(v);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}
	
	public DataBuffer appendJSON(Object v){
		try {
			writer.writeString(JSON.toJSONString(v));
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	} 
	
	public DataBuffer appendXmlObject(Object v){
		try {
			writer.writeXmlObject(v);
			return this;
		} catch (Exception e) {
			throw new BufferException(e);
		}
	} 
	
	public DataBuffer appendBean(Object v){
		try {
			writer.writeBean(v);
			return this;
		} catch (Exception e) {
			throw new BufferException(e);
		}
	} 
	
	public DataBuffer appendString(String v){
		try {
			writer.writeString(v);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}
	
	public DataBuffer appendBoolean(boolean v){
		try {
			writer.writeBoolean(v);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	} 
	
	public DataBuffer appendBytes(byte[] bytes){
		try {
			writer.writeBytes(bytes);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	} 
	
	
	public DataBuffer appendUTFBytes(byte[] s){
		try {
			writer.writeUTFBytes(s);
			return this;
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}
	
	/**
	 * 所有数据的byte数组
	 * @return
	 */
	public byte[] toByteArray(){
		try {
			return writer.toByteArray();
		} catch (IOException e) {
			throw new BufferException(e);
		}
	}
}
