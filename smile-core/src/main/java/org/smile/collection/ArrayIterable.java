package org.smile.collection;

import java.lang.reflect.Array;
import java.util.AbstractList;
/**
 * 一个把数组封装成list兼容循环使用
 * @author 胡真山
 *
 */
public class ArrayIterable<E> extends AbstractList<E>{
	/**真实的数据数组*/
	private Object array;
	/**数组长度*/
	private int length;
	
	public ArrayIterable(Object array){
		this.array=array;
		length=Array.getLength(array);
	}

	@Override
	public int size() {
		return length;
	}

	@Override
	public E get(int index) {
		return (E) Array.get(array, index);
	}
}
