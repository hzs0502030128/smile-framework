package org.smile.commons;

import java.io.IOException;
import java.util.Collection;
/**
 * @author 胡真山
 * @Date 2016年1月20日
 */
public class StringBand implements Appendable,CharSequence{
	/**
	 * 默认初始化字符串数组长度
	 */
	private static final int DEFAULT_ARRAY_CAPACITY = 16;
	/** 实际字符串数组 */
	private String[] array;
	/**当前字符串数组的索引*/
	private int index;
	/**所有字符串的长度和*/
	private int length;
	/**
	 * Creates an empty <code>StringBand</code>.
	 */
	public StringBand() {
		array = new String[DEFAULT_ARRAY_CAPACITY];
	}

	/**
	 * Creates an empty <code>StringBand</code> with provided capacity.
	 * Capacity refers to internal string array (i.e. number of
	 * joins) and not the total string size.
	 */
	public StringBand(int initialCapacity) {
		if (initialCapacity <= 0) {
			throw new IllegalArgumentException("Invalid initial capacity");
		}
		array = new String[initialCapacity];
	}

	/**
	 * Creates <code>StringBand</code> with provided content.
	 */
	public StringBand(String s) {
		this();
		array[0] = s;
		index = 1;
		length = s.length();
	}

	public StringBand(Object o) {
		this(String.valueOf(o));
	}


	/**
	 * Appends boolean value.
	 */
	public StringBand append(boolean b) {
		return append(b ? Strings.TRUE : Strings.FALSE);
	}

	/**
	 * Appends double value.
	 */
	public StringBand append(double d) {
		return append(Double.toString(d));
	}

	/**
	 * Appends float value.
	 */
	public StringBand append(float f) {
		return append(Float.toString(f));
	}

	/**
	 * Appends int value.
	 */
	public StringBand append(int i) {
		return append(Integer.toString(i));
	}

	/**
	 * Appends long value.
	 */
	public StringBand append(long l) {
		return append(Long.toString(l));
	}

	/**
	 * Appends short value.
	 */
	public StringBand append(short s) {
		return append(Short.toString(s));
	}

	/**
	 * Appends a character. This is <b>not</b> efficient
	 * as in <code>StringBuilder</code>, since new string is created.
	 */
	@Override
	public StringBand append(char c) {
		return append(String.valueOf(c));
	}

	/**
	 * Appends byte value.
	 */
	public StringBand append(byte b) {
		return append(Byte.toString(b));
	}
	/**
	 * 添加一个字符串 并两加上双引号
	 * @param str
	 * @return
	 */
	public StringBand appendDquote(String str){
		return append("\""+str+"\"");
	}
	/**
	 * 添加一个字符串 并两加上单引号
	 * @param str
	 * @return
	 */
	public StringBand appendSquote(String str){
		return append("\'"+str+"\'");
	}
	/**
	 * Appends string representation of an object.
	 * If <code>null</code>, the <code>'null'</code> string
	 * will be appended.
	 */
	public StringBand append(Object obj) {
		return append(String.valueOf(obj));
	}
	
	public StringBand append(Object[] array){
		for(Object obj:array){
			append(obj);
		}
		return this;
	}
	
	public <T> StringBand  append(Collection<T> list){
		for(T t:list){
			append(t);
		}
		return this;
	}
	/**
	 * 添加一个字符数组
	 * @param chars
	 * @return
	 */
	public StringBand append(char[] chars){
		return append(new String(chars));
	}
	
	/**
	 * 添加一个字符数组
	 * @param chars
	 * @return
	 */
	public StringBand append(char[] chars,int offset,int len){
		return append(new String(chars,offset,len));
	}

	/**
	 * Appends a string.
	 */
	public StringBand append(String s) {
		if (s == null) {
			s = Strings.NULL;
		}

		if (index >= array.length) {
			expandCapacity();
		}

		array[index++] = s;
		length += s.length();
		
		return this;
	}


	/**
	 * Returns array capacity.
	 */
	public int capacity() {
		return array.length;
	}

	/**
	 * Returns total string length.
	 */
	@Override
	public int length() {
		return length;
	}

	/**
	 * Returns current index of string array.
	 */
	public int index() {
		return index;
	}

	/**
	 * Specifies the new index.
	 */
	public void setIndex(int newIndex) {
		if (newIndex < 0) {
			throw new ArrayIndexOutOfBoundsException(newIndex);
		}

		if (newIndex > array.length) {
			String[] newArray = new String[newIndex];
			System.arraycopy(array, 0, newArray, 0, index);
			array = newArray;
		}

		if (newIndex > index) {
			for (int i = index; i < newIndex; i++) {
				array[i] = Strings.EMPTY;
			}
		} else if (newIndex < index) {
			for (int i = newIndex; i < index; i++) {
				array[i] = null;
			}
		}
		index = newIndex;
		length = calculateLength();
	}


	/**
	 * Returns char at given position.
	 * This method is <b>not</b> fast as it calculates
	 * the right string array element and the offset!
	 */
	@Override
	public char charAt(int pos) {
		int len = 0;
		for (int i = 0; i < index; i++) {
			int newlen = len + array[i].length();
			if (pos < newlen) {
				return array[i].charAt(pos - len);
			}
			len = newlen;
		}
		throw new IllegalArgumentException("Invalid char index");
	}

	/**
	 * Returns string at given position.
	 */
	public String stringAt(int index) {
		if (index >= this.index) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return array[index];
	}

	/**
	 * Joins together all strings into one.
	 */
	public String toString() {
		return new SharedString(toCharArray()).toString();
	}
	/**
	 * 子字符串
	 * @param off
	 * @param len
	 * @return
	 */
	public String subString(int off,int len) {
		return new SharedString(toCharArray(off, len)).toString();
	}

	/**
	 * 转成字符串数组
	 * @param off
	 * @param len
	 * @return
	 */
	public char[] toCharArray(int off,int len){
		// special cases
		int end=off+len;
		if(length<off||end>length){
			throw new ArrayIndexOutOfBoundsException("off "+off+" len "+len+" end "+end);
		}
		// join chars
		char[] destination = new char[len];
		//读取字符结束的标记
		int readEnd = 0;
		//本段字符串的复制起始
		int currentStart=0;
		//本段字符串的复制结束
		int currentEnd=-1;
		//已经复制了的长度
		int writeLen=0;
		for (int i = 0; i < index; i++) {
			String s = array[i];
			readEnd+=s.length();
			//起始读取
			if(readEnd>off){
				//没有读取时需要计算起始读取，已经读取过了从0开始复制
				currentStart=writeLen==0?s.length()-readEnd+off:0;
				if(readEnd<end){//没有读取完时
					currentEnd=s.length();
					s.getChars(currentStart,currentEnd,destination, writeLen);
					writeLen+=currentEnd-currentStart;
				}else{//读取完成
					currentEnd=s.length()+end-readEnd;
					s.getChars(currentStart,currentEnd,destination, writeLen);
					writeLen+=currentEnd-currentStart;
					break;
				}
			}
		}
		return destination;
	}

	/**
	 * Expands internal string array by multiplying its size by 2.
	 */
	protected void expandCapacity() {
		String[] newArray = new String[array.length << 1];
		System.arraycopy(array, 0, newArray, 0, index);
		array = newArray;
	}

	/**
	 * Calculates string length.
	 */
	protected int calculateLength() {
		int len = 0;
		for (int i = 0; i < index; i++) {
			len += array[i].length();
		}
		return len;
	}
	/**
	 * 是否是空的字符串
	 * @return
	 */
	public boolean isEmpty(){
		return length==0;
	}
	/**
	 * 清空字符带
	 */
	public void clear(){
		setIndex(0);
	}
	/**
	 * 转成字符数组
	 * @return
	 */
	public char[] toCharArray(){
		// join chars
		char[] destination = new char[length];
		int start = 0;
		for (int i = 0; i < index; i++) {
			String s = array[i];
			int len = s.length();
			s.getChars(0, len, destination, start);
			start += len;
		}
		return destination;
	}

	@Override
	public StringBand append(CharSequence csq) throws IOException {
		if(csq==null){
			return append(Strings.NULL);
		}
		return append(csq.toString());
	}

	@Override
	public StringBand append(CharSequence csq, int start, int end) throws IOException {
		return append(csq.subSequence(start, end));
	}

	@Override
	public String subSequence(int start, int end) {
		int len=end-start;
		return new SharedString(toCharArray(start,len)).toString();
	}
}
