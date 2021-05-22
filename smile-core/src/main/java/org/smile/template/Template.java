package org.smile.template;

import java.io.Writer;
/**
 * 文件模板
 * @author 胡真山
 * 2015年12月4日
 */
public interface Template {
	
	public static final String FREE_MARKER="freemarker";
	
	public static final String GROOVY="groovy";
	
	public static final String SIMPLE="simple";
	
	public static final String NONE="none";
	
	public static final String EXPRESSION="expression";
	
	public static final String SMILE="smile";
	
	/**
	 * 处理模板内容放入一个writer中
	 * @param params 源参数
	 * @param out 输入目标
	 */
	public void process(Object params,Writer out);
	/**
	 * 模板处理成一个字符串
	 * @param params
	 * @return
	 */
	public String processToString(Object params);
	/**
	 * 获取源
	 * @return
	 */
	public Object getTemplateSource();


	
}
