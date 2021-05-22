package org.smile.collection;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * 数组操作工具类
 * @author 胡真山
 * @Date 2016年1月14日
 */
public class ArrayUtils {
	/**
	 * 验证一个数组中的值 是否与给定的值一样 equals 方法
	 * @param array 要验证的数组
	 * @param checkIndex 需要验证的索引
	 * @param checkVal 用来验证的值
	 * @return
	 */
	public static <T> boolean check(T[] array,int[] checkIndex,T[] checkVal){
		for(int i=0;i<checkVal.length;i++){
			if(!checkVal[i].equals(array[checkIndex[i]])){
				return false;
			}
		}
		return true;
	}
	/**
	 * 数组是否为空  
	 * null 或 [] 时为true
	 *  其它 为false
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object array){
		if(array==null){
			return true;
		}
		return Array.getLength(array)==0;
	}
	/**
	 * 是否为空的
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array){
		if(array==null){
			return true;
		}
		return array.length==0;
	}
	/**
	 * 不为空 null 或 [] 时为false
	 * @param array
	 * @return
	 */
	public static boolean notEmpty(Object array){
		return !isEmpty(array);
	}
	
	/**
	 * 数组是否为空 或者空数组
	 * @param array
	 * @return
	 */
	public static boolean notEmpty(Object[] array){
		return !isEmpty(array);
	}
	
	/**数组中是否存在一个对象*/
	public static <E> boolean arrayContains(E[] array, E e){
		for(E arrayE:array){
			if(arrayE.equals(e)){
				return true;
			}
		}
		return false;
	}
	/***
	 * 每一个对象都equals 
	 * @param a 
	 * @param b
	 * @return 是否每一个对象都满足equals
	 */
	public static boolean isEquals(Object[] a,Object[] b){
		if(a.length!=b.length){
			return false;
		}
		for(int i=0;i<a.length;i++){
			if(!a[i].equals(b[i])){
				return false;
			}
		}
		return true;
	}
	/**
	 * 按索引获取数组元素
	 * @param array
	 * @param index
	 * @return
	 */
	public static Object get(Object[] array,int index){
		return array[index];
	}
	/**
	 * 以索引获取数组元素
	 * @param array
	 * @param index
	 * @return
	 */
	public static Object get(Object array,int index){
		return Array.get(array, index);
	}
	/**
	 * 获取数组长度
	 * @param array
	 * @return
	 */
	public static int size(Object array){
		return Array.getLength(array);
	}
	
	/**
	 * Wraps elements into an array.
	 */
	public static <T> T[] array(T... elements) {
		return elements;
	}

	/**
	 * Wraps elements into an array.
	 */
	public static byte[] bytes(byte... elements) {
		return elements;
	}

	/**
	 * Wraps elements into an array.
	 */
	public static char[] chars(char... elements) {
		return elements;
	}

	/**
	 * Wraps elements into an array.
	 */
	public static short[] shorts(short... elements) {
		return elements;
	}

	/**
	 * Wraps elements into an array.
	 */
	public static int[] ints(int... elements) {
		return elements;
	}

	/**
	 * Wraps elements into an array.
	 */
	public static long[] longs(long... elements) {
		return elements;
	}

	/**
	 * Wraps elements into an array.
	 */
	public static float[] floats(float... elements) {
		return elements;
	}

	/**
	 * Wraps elements into an array.
	 */
	public static double[] doubles(double... elements) {
		return elements;
	}

	/**
	 * Wraps elements into an array.
	 */
	public static boolean[] booleans(boolean... elements) {
		return elements;
	}
	
	/**
	 * reverse then array  [1,2,3,4,5] -> [5,4,3,2,1]
	 * @param array
	 * @return
	 */
	public static void reverse(Object[] array){
		int lastIndex=array.length-1;
		for(int i=0;i<array.length/2;i++){
			Object temp=array[i];
			array[i]=array[lastIndex-i];
			array[lastIndex-i]=temp;
		}
	}
	/**
	 * reverse then array  ['1','2','3','4','5'] -> ['5','4','3','2','1']
	 * @param array
	 * @return
	 */
	public static void reverse(char[] chars){
		int lastIndex=chars.length-1;
		for(int i=0;i<chars.length/2;i++){
			char temp=chars[i];
			chars[i]=chars[lastIndex-i];
			chars[lastIndex-i]=temp;
		}
	}
	
	/**
	 * reverse then array  [1,2,3,4,5] -> [5,4,3,2,1]
	 * @param array
	 * @return
	 */
	public static void reverse(Object array){
		int length=Array.getLength(array);
		int lastIndex=length-1;
		for(int i=0;i<length/2;i++){
			Object temp=Array.get(array, i);
			Array.set(array,i,Array.get(array,lastIndex-i));
			Array.set(array,lastIndex-i,temp);
		}
	}
    /**
     * 从设数组长度
     * @param buffer
     * @param newSize
     * @return
     */
	public static <T> T[] resize(T[] buffer, int newSize) {
		Class<T> componentType = (Class<T>) buffer.getClass().getComponentType();
		T[] temp = (T[]) Array.newInstance(componentType, newSize);
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}
	/**
	 * Resizes a <code>byte</code> array.
	 */
	public static byte[] resize(byte buffer[], int newSize) {
		byte temp[] = new byte[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>char</code> array.
	 */
	public static char[] resize(char buffer[], int newSize) {
		char temp[] = new char[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>short</code> array.
	 */
	public static short[] resize(short buffer[], int newSize) {
		short temp[] = new short[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>int</code> array.
	 */
	public static int[] resize(int buffer[], int newSize) {
		int temp[] = new int[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>long</code> array.
	 */
	public static long[] resize(long buffer[], int newSize) {
		long temp[] = new long[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>float</code> array.
	 */
	public static float[] resize(float buffer[], int newSize) {
		float temp[] = new float[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>double</code> array.
	 */
	public static double[] resize(double buffer[], int newSize) {
		double temp[] = new double[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>boolean</code> array.
	 */
	public static boolean[] resize(boolean buffer[], int newSize) {
		boolean temp[] = new boolean[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}
	
	/**
	 * Appends an element to array.
	 */
	public static <T> T[] append(T[] buffer, T newElement) {
		T[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>String</code> array.
	 */
	public static String[] append(String buffer[], String newElement) {
		String[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>byte</code> array.
	 */
	public static byte[] append(byte buffer[], byte newElement) {
		byte[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>char</code> array.
	 */
	public static char[] append(char buffer[], char newElement) {
		char[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>short</code> array.
	 */
	public static short[] append(short buffer[], short newElement) {
		short[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>int</code> array.
	 */
	public static int[] append(int buffer[], int newElement) {
		int[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>long</code> array.
	 */
	public static long[] append(long buffer[], long newElement) {
		long[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>float</code> array.
	 */
	public static float[] append(float buffer[], float newElement) {
		float[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>double</code> array.
	 */
	public static double[] append(double buffer[], double newElement) {
		double[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>boolean</code> array.
	 */
	public static boolean[] append(boolean buffer[], boolean newElement) {
		boolean[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}
	/**
	 * 把一个数据添加到另一个数据中组成新的数组
	 * @param buffer 原数组
	 * @param add 要增加的数组
	 * @return 新数组
	 */
	public static <T> T[] append(T[] buffer,T[] add){
		T[] t=resize(buffer, buffer.length+add.length);
		System.arraycopy(add, 0, t, buffer.length, add.length);
		return t;
	}
	
	/**
	 * 把一个数据添加到另一个数据中组成新的数组
	 * @param buffer 原数组
	 * @param add 要增加的数组
	 * @return 新数组
	 */
	public static byte[] append(byte[] buffer,byte[] add){
		byte[] t=resize(buffer, buffer.length+add.length);
		System.arraycopy(add, 0, t, buffer.length, add.length);
		return t;
	}
	
	/**
	 * Returns subarray.
	 */
	public static <T> T[] subarray(T[] buffer, int offset, int length) {
		Class<T> componentType = (Class<T>) buffer.getClass().getComponentType();
		return subarray(buffer, offset, length, componentType);
	}

	/**
	 * Returns subarray.
	 */
	@SuppressWarnings({"unchecked"})
	public static <T> T[] subarray(T[] buffer, int offset, int length, Class<T> componentType) {
		T[] temp = (T[]) Array.newInstance(componentType, length);
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}
	
	/**
	 * Returns subarray.
	 */
	public static String[] subarray(String[] buffer, int offset, int length) {
		String temp[] = new String[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static byte[] subarray(byte[] buffer, int offset, int length) {
		byte temp[] = new byte[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static char[] subarray(char[] buffer, int offset, int length) {
		char temp[] = new char[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static short[] subarray(short[] buffer, int offset, int length) {
		short temp[] = new short[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static int[] subarray(int[] buffer, int offset, int length) {
		int temp[] = new int[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static long[] subarray(long[] buffer, int offset, int length) {
		long temp[] = new long[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static float[] subarray(float[] buffer, int offset, int length) {
		float temp[] = new float[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static double[] subarray(double[] buffer, int offset, int length) {
		double temp[] = new double[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static boolean[] subarray(boolean[] buffer, int offset, int length) {
		boolean temp[] = new boolean[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}
	
	/**
	 * Converts to primitive array.
	 */
	public static byte[] values(Byte[] array) {
		byte[] dest = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			Byte v = array[i];
			if (v != null) {
				dest[i] = v.byteValue();
			}
		}
		return dest;
	}
	/**
	 * Converts to object array.
	 */
	public static Byte[] valuesOf(byte[] array) {
		Byte[] dest = new Byte[array.length];
		for (int i = 0; i < array.length; i++) {
			dest[i] = Byte.valueOf(array[i]);
		}
		return dest;
	}
	
	public static Character[] valueOf(char[] array){
		Character[] dest = new Character[array.length];
		for (int i = 0; i < array.length; i++) {
			dest[i] = Character.valueOf(array[i]);
		}
		return dest;
	}


	/**
	 * Converts to primitive array.
	 */
	public static char[] values(Character[] array) {
		char[] dest = new char[array.length];
		for (int i = 0; i < array.length; i++) {
			Character v = array[i];
			if (v != null) {
				dest[i] = v.charValue();
			}
		}
		return dest;
	}
	/**
	 * Converts to object array.
	 */
	public static Character[] valuesOf(char[] array) {
		Character[] dest = new Character[array.length];
		for (int i = 0; i < array.length; i++) {
			dest[i] = Character.valueOf(array[i]);
		}
		return dest;
	}


	/**
	 * Converts to primitive array.
	 */
	public static short[] values(Short[] array) {
		short[] dest = new short[array.length];
		for (int i = 0; i < array.length; i++) {
			Short v = array[i];
			if (v != null) {
				dest[i] = v.shortValue();
			}
		}
		return dest;
	}
	/**
	 * Converts to object array.
	 */
	public static Short[] valuesOf(short[] array) {
		Short[] dest = new Short[array.length];
		for (int i = 0; i < array.length; i++) {
			dest[i] = Short.valueOf(array[i]);
		}
		return dest;
	}


	/**
	 * Converts to primitive array.
	 */
	public static int[] values(Integer[] array) {
		int[] dest = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			Integer v = array[i];
			if (v != null) {
				dest[i] = v.intValue();
			}
		}
		return dest;
	}
	/**
	 * Converts to object array.
	 */
	public static Integer[] valuesOf(int[] array) {
		Integer[] dest = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			dest[i] = Integer.valueOf(array[i]);
		}
		return dest;
	}


	/**
	 * Converts to primitive array.
	 */
	public static long[] values(Long[] array) {
		long[] dest = new long[array.length];
		for (int i = 0; i < array.length; i++) {
			Long v = array[i];
			if (v != null) {
				dest[i] = v.longValue();
			}
		}
		return dest;
	}
	/**
	 * Converts to object array.
	 */
	public static Long[] valuesOf(long[] array) {
		Long[] dest = new Long[array.length];
		for (int i = 0; i < array.length; i++) {
			dest[i] = Long.valueOf(array[i]);
		}
		return dest;
	}


	/**
	 * Converts to primitive array.
	 */
	public static float[] values(Float[] array) {
		float[] dest = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			Float v = array[i];
			if (v != null) {
				dest[i] = v.floatValue();
			}
		}
		return dest;
	}
	/**
	 * Converts to object array.
	 */
	public static Float[] valuesOf(float[] array) {
		Float[] dest = new Float[array.length];
		for (int i = 0; i < array.length; i++) {
			dest[i] = Float.valueOf(array[i]);
		}
		return dest;
	}


	/**
	 * Converts to primitive array.
	 */
	public static double[] values(Double[] array) {
		double[] dest = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			Double v = array[i];
			if (v != null) {
				dest[i] = v.doubleValue();
			}
		}
		return dest;
	}
	/**
	 * Converts to object array.
	 */
	public static Double[] valuesOf(double[] array) {
		Double[] dest = new Double[array.length];
		for (int i = 0; i < array.length; i++) {
			dest[i] = Double.valueOf(array[i]);
		}
		return dest;
	}


	/**
	 * Converts to primitive array.
	 */
	public static boolean[] values(Boolean[] array) {
		boolean[] dest = new boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			Boolean v = array[i];
			if (v != null) {
				dest[i] = v.booleanValue();
			}
		}
		return dest;
	}
	/**
	 * Converts to object array.
	 */
	public static Boolean[] valuesOf(boolean[] array) {
		Boolean[] dest = new Boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			dest[i] = Boolean.valueOf(array[i]);
		}
		return dest;
	}

	/**
	 * converts to object array
	 * @param collection
	 * @return
	 */
	public static Object[] valuesOf(Collection collection){
		return collection.toArray();
	}
}
