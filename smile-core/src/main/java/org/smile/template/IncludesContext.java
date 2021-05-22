package org.smile.template;

import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.io.BufferedReader;
import org.smile.util.RegExp;
/**
 * 包括的内容容器
 * @author 胡真山
 */
public class IncludesContext{
	/**模板内容*/
	private Map<String,Reader> templates=new ConcurrentHashMap<String,Reader>();
	/**用于标记引用其它模板*/
	private static final RegExp includeReg=new RegExp("<#include .+ *>");
	//获取名称开始索引
	private static final int NAME_INDEX=10;
	
	private static final RegExp nameReplaceReg=new RegExp("[\'\" ]+");
	/**
	 * 添加模板片断
	 * @param name
	 * @param reader
	 */
	public void addInclude(String name,Reader reader){
		templates.put(name, reader);
	}
	/**
	 * 获取模板片断
	 * @param name
	 * @return
	 */
	public Reader getInclude(String name){
		return templates.get(name);
	}
	
	/**
	 * 判断是否为空的内容
	 * 没有其它模板片断是返回true
	 * @return
	 */
	public boolean isEmpty(){
		return templates.isEmpty();
	}
	
	public boolean notEmpty(){
		return !templates.isEmpty();
	}
	/**
	 * 解析出模板片断的名称
	 * @param includeTxt
	 * @return
	 */
	private String getIncludeName(String includeTxt){
		String name=includeTxt.substring(NAME_INDEX, includeTxt.length()-1);
		return nameReplaceReg.replaceAll(name, "");
	} 
	/**
	 * 查找出所有的模板片断引用标签内容
	 * @param templateText
	 * @return
	 */
	public String[] findInclude(String templateText){
		return includeReg.find(templateText);
	}
	
	public String replaceInclude(String[] includeTxts,String templateText){
		try {
			for(String text:includeTxts){
				String name=getIncludeName(text);
				Reader reader=templates.get(name);
				if(reader==null){
					throw new TemplateHandlerException("The include name does not exist :"+name);
				}
				BufferedReader dis=new BufferedReader(reader);
				templateText=templateText.replace(text,dis.readToString());
			}
			return templateText;
		} catch (Exception e) {
			throw new TemplateHandlerException("process template error,content "+templateText, e);
		}
	}
}
