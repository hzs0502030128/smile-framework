package org.smile.io;

import java.io.Writer;

import org.smile.commons.SharedString;
import org.smile.io.buff.CharBand;

public class StringWriter extends Writer implements CharSequence{
	/**缓存写入的字符*/
	protected final CharBand buffer;
	/**
	 * 字符带的每一个带的长度
	 * @param minInitSize
	 */
	public StringWriter(int minInitSize){
		super();
		this.buffer=new CharBand(minInitSize);
	}
	/***/
	public StringWriter(){
		super();
		this.buffer=new CharBand();
	}
	
	@Override
	public void write(char[] cbuf, int off, int len){
		synchronized (lock) {
			this.buffer.append(cbuf, off, len);
		}
	}
	
	@Override
	public void write(String string){
		synchronized (lock) {
			this.buffer.append(string);
		}
	}
	
	public StringWriter write(CharSequence chars){
		synchronized (lock) {
			this.buffer.append(chars);
			return this;
		}
	}
	
	public StringWriter write(char c){
		synchronized (lock) {
			this.buffer.append(c);
			return this;
		}
	}

	@Override
	public void flush() {
		
	}

	@Override
	public void close() {
		
	}

	@Override
	public String toString() {
		return this.buffer.toString();
	}
	
	/**
	 * 已写入的长度
	 * @return
	 */
	public int size(){
		return this.buffer.length();
	}
	/**
	 * 只取一部分转换在字符串
	 * @param start 起始索引
	 * @param len 长度
	 * @return
	 */
	public String toString(int start,int len){
		return new SharedString(this.buffer.toArray(start, len)).toString();
	}
	
	@Override
	public int length() {
		return this.buffer.length();
	}
	
	@Override
	public char charAt(int index) {
		return this.buffer.charAt(index);
	}
	
	@Override
	public CharSequence subSequence(int start, int end) {
		return this.buffer.subSequence(start, end);
	}
}
