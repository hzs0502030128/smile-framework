package org.smile.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序工具
 * @author 胡真山
 *
 */
public class SortUtils {
	/**
	 * 快速排序
	 * 
	 * @param arr 需要排序的数组
	 * @param low 
	 * @param high
	 */
	public static void quickSort(int arr[], int low, int high) {
		int l = low;
		int h = high;
		int povit = arr[low];

		while (l < h) {
			while (l < h && arr[h] >= povit) {
				h--;
			}

			if (l < h) {
				int temp = arr[h];
				arr[h] = arr[l];
				arr[l] = temp;
				l++;

			}

			while (l < h && arr[l] <= povit) {
				l++;
			}

			if (l < h) {
				int temp = arr[h];
				arr[h] = arr[l];
				arr[l] = temp;
				h--;

			}
		}
		// 低位排序
		if (l > low) {
			quickSort(arr, low, h - 1);
		}
		// 高位排序
		if (h < high) {
			quickSort(arr, l + 1, high);
		}
	}
	/**
	 * 快速排序
	 * @param array
	 * @param c
	 */
	public static <T> void quickSort(T[] array,Comparator<? super T> c) {
		quickSort(array,0,array.length-1,c);
	}
	
	public static <T extends Comparable<? super T>> void quickSort(T[] array){
		quickSort(array,new Comparator<T>(){
			@Override
			public int compare(T o1, T o2) {
				return o1.compareTo(o2);
			}
		});
	}
	
	public static <T extends Comparable<? super T>> void quickSort(T[] array,int low,int high){
		quickSort(array,low,high,new Comparator<T>(){
			@Override
			public int compare(T o1, T o2) {
				return o1.compareTo(o2);
			}
		});
	}
	/**
	 * 例序排序
	 * @param array
	 */
	public static <T extends Comparable<? super T>> void quickSortReverse(T[] array){
		quickSort(array,new Comparator<T>(){
			@Override
			public int compare(T o1, T o2) {
				return o2.compareTo(o1);
			}
		});
	}
	
	/**
	 * 排序
	 * @param array
	 */
	public static <T> void quickSort(T[] array,int low,int high, Comparator<? super T> c) {
		int l = low;
		int h = high;
		T povit = array[low];

		while (l < h) {
			while (l < h && c.compare(array[h] , povit)>=0) {
				h--;
			}

			if (l < h) {
				T temp = array[h];
				array[h] = array[l];
				array[l] = temp;
				l++;
			}

			while (l < h && c.compare(array[l] , povit)<=0) {
				l++;
			}

			if (l < h) {
				T temp = array[h];
				array[h] = array[l];
				array[l] = temp;
				h--;
			}
		}
		// 低位排序
		if (l > low) {
			quickSort(array, low, h - 1,c);
		}
		// 高位排序
		if (h < high) {
			quickSort(array, l + 1, high,c);
		}
	}
	/**
	 * 快速排序
	 * @param array
	 */
	public static void  quickSort(int[] array){
		quickSort(array, 0, array.length-1);
	}
	/**
	 * 排序 参考{@link Arrays} sort
	 * @param array
	 */
	public static <T extends Comparable<? super T>> void sort(T[] array){
		Arrays.sort(array);
	}
	/**
	 * 排序  参考{@link Arrays} sort
	 * @param array
	 */
	public static <T> void sort(T[] array, Comparator<? super T> c) {
		Arrays.sort(array,c);
	}
	
	/**
	 * 例序排序  参考{@link Arrays} sort
	 * @param array
	 */
	public static <T extends Comparable<? super T>> void sortReverse(T[] array) {
		Arrays.sort(array,new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o2.compareTo(o1);
			}
		});
	}
	/**
	 * 排序  参考{@link Collections} sort
	 * @param list
	 */
	public static <T extends Comparable<? super T>> void sort(List<T> list) {
		Collections.sort(list);
	}
	
	/**
	 * 倒序排序  参考{@link Collections} sort
	 * @param list
	 */
	public static <T extends Comparable<? super T>> void sortReverse(List<T> list) {
		Collections.sort(list,new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o2.compareTo(o1);
			}
		});
	}
	/**
	 * 排序  参考{@link Collections} sort
	 * @param list
	 * @param c
	 */
	public static <T> void sort(List<T> list, Comparator<? super T> c) {
		Collections.sort(list, c);
	}
	
}
