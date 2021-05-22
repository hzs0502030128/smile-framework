package org.smile.report.excel;

import java.util.HashMap;
import java.util.Map;

import org.smile.report.poi.ObjectMergeSet;

/**
 * 设置要合并的列
 * @author 胡真山
 */
public class MergeConfig extends ObjectMergeSet{
	/***
	 * 要合并的列名
	 */
	protected String[] mergeName;
	/**名称与索引的映射*/
	protected Map<String,Integer> nameIndex=new HashMap<String,Integer>();
	/**
	 * @param keyName
	 * @param mergeColumn 要合并的列的索引
	 */
	public MergeConfig(String keyPropertyName, Integer[] mergeColumn){
		this.propertyName=new String[]{keyPropertyName};
		this.mergeColumn=mergeColumn;
	}
	
	/**
	 * 以标题名称来标记合并
	 * @param keyName
	 * @param mergeName
	 */
	public MergeConfig(String keyPropertyName, String[] mergeName){
		this.propertyName=new String[]{keyPropertyName};
		this.mergeName=mergeName;
	}
	
	/**
	 * @param keyName
	 * @param mergeColumn 要合并的列的索引
	 */
	public MergeConfig(String[] keyPropertyName, Integer[] mergeColumn){
		this.propertyName=keyPropertyName;
		this.mergeColumn=mergeColumn;
	}
	
	/**
	 * 以标题名称来标记合并
	 * @param keyName 用于确认是否需要合并的关键列
	 * @param mergeName 要合并的列
	 */
	public MergeConfig(String[] keyPropertyName, String[] mergeName){
		this.propertyName=keyPropertyName;
		this.mergeName=mergeName;
	}
	/**
	 * 从xls的标题初始化出要合并的列的索引
	 * @param names 所有的列
	 * @param firstCellNum 标题名称开始的列索引
	 */
	protected void initMerge(String[] names,int firstCellNum){
		initNameIndex(names);
		if(mergeColumn==null){
			initMergeColumn(names,firstCellNum);
		}
	}
	/***
	 * 获取列的索引值
	 * @param name 列的名称
	 * @return 从0开始
	 */
	public Integer getColumnIndex(String name){
		return nameIndex.get(name);
	}
	/**
	 * 用来初始化列名的索引值映射
	 * @param names 
	 */
	protected void initNameIndex(String[] names){
		int i=0;
		for(String n:names){
			nameIndex.put(n, i++);
		}
	}
	
	protected void initMergeColumn(String[] names,int firstCellNum){
		mergeColumn=new Integer[mergeName.length];
		int i=firstCellNum;
		for(String n:mergeName){
			Integer idx=nameIndex.get(n);
			if(idx==null){
				throw new NullPointerException("不存在的数据名称列:"+n);
			}
			mergeColumn[i]=idx;
			i++;
		}
	}

}
