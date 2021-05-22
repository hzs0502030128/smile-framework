package org.smile.template;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyHandlers;
import org.smile.beans.converter.BeanException;
import org.smile.collection.SoftHashMap;
import org.smile.collection.WeakHashMap;
import org.smile.commons.StringBand;
import org.smile.commons.Strings;
import org.smile.log.LoggerHandler;
import org.smile.util.StringUtils;

public class StringTemplateParser implements TemplateParser,LoggerHandler {
	/** 是否对key找不到值替换的时候进行替换 */
	protected boolean replaceMissingKey = true;
	/** key找不到替换的时候使用此替换 */
	protected String missingKeyReplacement;
	/**
	 * 是否需要解决转义符
	 */
	protected boolean resolveEscapes = true;
	/**
	 * 指令 开始标记
	 */
	protected String macroStart = DEFAULT_MACRO_START;
	/**
	 * 指令结束标记
	 */
	protected String macroEnd = DEFAULT_MACRO_END;
	/** 转义符 */
	protected char escapeChar = '\\';
	/**是否还需要对值进行解析*/
	protected boolean parseValues;
	/**对模板的缓存*/
	protected Map<String, MultiFragment> caches;
	
	/**
	 * 默认使用${}为占位表达式
	 */
	public StringTemplateParser(){
		this.caches=SoftHashMap.newConcurrentInstance();
	}
	/**
	 * 
	 * @param macroStart 占位表达式前缀
	 * @param macroEnd 占位表达式后缀
	 */
	public StringTemplateParser(String macroStart,String macroEnd ){
		this.macroStart=macroStart;
		this.macroEnd=macroEnd;
		this.caches=SoftHashMap.newConcurrentInstance();
	}
	/**
	 * 缓存的编译信息是否是采用弱引用
	 * @param macroStart
	 * @param macroEnd
	 * @param weakRef
	 */
	public StringTemplateParser(String macroStart,String macroEnd,boolean weakRef){
		this.macroStart=macroStart;
		this.macroEnd=macroEnd;
		if(weakRef){
			this.caches=WeakHashMap.newConcurrentInstance();
		}else{
			this.caches=SoftHashMap.newConcurrentInstance();
		}
	}

	public boolean isReplaceMissingKey() {
		return replaceMissingKey;
	}

	@Override
	public void setReplaceMissingKey(boolean replaceMissingKey) {
		this.replaceMissingKey = replaceMissingKey;
	}

	public String getMissingKeyReplacement() {
		return missingKeyReplacement;
	}

	@Override
	public void setMissingKeyReplacement(String missingKeyReplacement) {
		this.missingKeyReplacement = missingKeyReplacement;
	}

	public boolean isResolveEscapes() {
		return resolveEscapes;
	}

	/**
	 * Specifies if escaped values should be resolved. In special usecases, when
	 * the same string has to be processed more then once, this may be set to
	 * <code>false</code> so escaped values remains.
	 */
	public void setResolveEscapes(boolean resolveEscapes) {
		this.resolveEscapes = resolveEscapes;
	}

	public String getMacroStart() {
		return macroStart;
	}

	/**
	 * Defines macro start string.
	 */
	@Override
	public void setMacroStart(String macroStart) {
		this.macroStart = macroStart;
	}

	public String getMacroEnd() {
		return macroEnd;
	}

	/**
	 * Defines macro end string.
	 */
	@Override
	public void setMacroEnd(String macroEnd) {
		this.macroEnd = macroEnd;
	}

	public char getEscapeChar() {
		return escapeChar;
	}

	/**
	 * Defines escape character.
	 */
	public void setEscapeChar(char escapeChar) {
		this.escapeChar = escapeChar;
	}

	public boolean isParseValues() {
		return parseValues;
	}

	/**
	 * Defines if macro values has to be parsed, too. By default, macro values
	 * are returned as they are.
	 */
	public void setParseValues(boolean parseValues) {
		this.parseValues = parseValues;
	}

	@Override
	public String parse(String template, MacroResolver macroResolver) {
		MultiFragment multi=caches.get(template);
		if(multi==null){
			multi=new MultiFragment(false);
			parseFragment(multi, template);
			caches.put(template, multi);
		}
		return multi.invoke(macroResolver);
	}
	/**
	 * 
	 * @param template
	 * @return
	 */
	@Override
	public MultiFragment fragment(String template){
		MultiFragment multi=caches.get(template);
		if(multi==null){
			multi=new MultiFragment(false);
			parseFragment(multi, template);
			caches.put(template, multi);
		}
		return multi;
	}
	/***
	 * 解析文本
	 * @param multi
	 * @param template
	 */
	private void parseFragment(MultiFragment multi, String template){
		int i = 0;
		int len = template.length();
		int startLen = macroStart.length();
		int endLen = macroEnd.length();

		while (i < len) {
			int ndx = template.indexOf(macroStart, i);
			if (ndx == -1) {
				String txt=i== 0 ? template : template.substring(i);
				multi.addSub(new ParseFragment(txt));
				break;
			}

			// check escaped
			int j = ndx - 1;
			boolean escape = false;
			int count = 0;

			while ((j >= 0) && (template.charAt(j) == escapeChar)) {
				escape = !escape;
				if (escape) {
					count++;
				}
				j--;
			}
			if (resolveEscapes) {
				multi.addSub(new ParseFragment(template.substring(i, ndx - count)));
			} else {
				multi.addSub(new ParseFragment(template.substring(i, ndx)));
			}
			if (escape == true) {
				multi.addSub(new ParseFragment(macroStart));
				i = ndx + startLen;
				continue;
			}

			// find macros end
			ndx += startLen;
			int ndx2=findEndIndex(template, ndx, macroStart, macroEnd);
			if (ndx2 ==-1) {
				throw new IllegalArgumentException("Invalid template, unclosed macro at: " + (ndx - startLen));
			}
			
			// 参数名称
			String name = StringUtils.trim(template.substring(ndx, ndx2));
			if(name.indexOf(macroStart)!=-1){//内容中还包含起始符
				MultiFragment subMulti=new MultiFragment(true);
				parseFragment(subMulti,name);
				multi.addSub(subMulti);
			}else{
				ParseFragment f=new ParseFragment(name,true);
				multi.addSub(f);
			}
			i = ndx2 + endLen;
		}
	}
	/***
	 * 查找结束符位置
	 * 使用栈的方式对表达式起始符进行匹配
	 * @param template
	 * @param start
	 * @param macroStart
	 * @param macroEnd
	 * @return
	 */
	private int findEndIndex(String template,int start,String macroStart,String macroEnd){
		Stack<Character> stack=new Stack<Character>();
		while(start<template.length()){
			int startIdx=template.indexOf(macroStart,start);
			int endIdx=template.indexOf(macroEnd,start);
			if((startIdx==-1)||endIdx<startIdx){
				if(stack.isEmpty()){
					return endIdx;
				}
				char c=stack.peek();
				if(c=='}'){
					stack.push('}');
				}else{
					stack.pop();
				}
				start=endIdx+macroEnd.length();
			}else{
				stack.push('{');
				start=startIdx+macroStart.length();
			}
		}
		return -1;
	}

	public static class BaseMacroResolver implements MacroResolver {
		/**获取map的内容*/
		protected PropertyHandler handler;
		/***/
		protected Object param;

		public BaseMacroResolver(Object map) {
			this.param = map;
			this.handler=PropertyHandlers.getHanlder(param.getClass());
		}

		protected Object getValue(String macroName) {
			if (param == null) {
				return null;
			}
			Object value=null;
			try {
				value = handler.getExpFieldValue(param,macroName);
			} catch (BeanException e) {
				logger.error(e);
			}
			return value;
		}

		@Override
		public String resolve(String macroName) {
			Object value = getValue(macroName);
			if (value == null) {
				return null;
			}
			return toString(value);
		}
		
		protected String toString(Object value){
			if(value instanceof Number){
				Number number=(Number)value;
				if(number.doubleValue()==number.longValue()){
					return String.valueOf(number.longValue());
				}else{
					return String.valueOf(number.doubleValue());
				}
			}
			return String.valueOf(value);
		}
	}

	/**
	 * 非数字和boolean型会加上单引号表式字符串
	 * @author 胡真山
	 *
	 */
	public static class QuoteMacroResolver extends BaseMacroResolver {

		public QuoteMacroResolver(Object map) {
			super(map);
		}

		@Override
		public String resolve(String macroName) {
			Object value = getValue(macroName);
			if (value instanceof Number || value instanceof Boolean) {
				return toString(value);
			}
			return "'" + value + "'";
		}
	}

	/**
	 * Creates commonly used {@link MacroResolver} that resolved macros in the
	 * provided map.
	 */
	public static MacroResolver createMacroResolver(final Object map) {
		return new BaseMacroResolver(map);
	}

	/**模板解析片断*/
	abstract class AbstractFragment implements Fragment{
		/**是否表达式*/
		boolean isExpress;
		/**片断调用返回片断运行后的内容**/
		abstract String invoke(MacroResolver macroResolver);
		/**对表达式片断进行调用*/
		@Override
		public String invokeExpress(MacroResolver macroResolver,String express){
			Object value=null;
			if (missingKeyReplacement != null || replaceMissingKey == false) {
				try {
					value = macroResolver.resolve(express);
				} catch (Exception ignore) {
					value = null;
				}
				if (value == null) {
					if (replaceMissingKey == true) {
						value = missingKeyReplacement;
					} else {
						value =macroStart+express+macroEnd ;
					}
				}
			} else {
				value = macroResolver.resolve(express);
				if (value == null) {
					value = Strings.EMPTY;
				}
			}
			if(parseValues){
				return parse(String.valueOf(value), macroResolver);
			}
			return String.valueOf(value);
		}
	}
	/**
	 * 多个片断
	 * @author 胡真山
	 */
	class MultiFragment extends AbstractFragment {
		/**子片断内容*/
		List<AbstractFragment> subFragment=new LinkedList<AbstractFragment>();
		
		MultiFragment(boolean isExpress){
			this.isExpress=isExpress;
		}
		@Override
		public boolean hasExpress(){
			for(AbstractFragment f:subFragment){
				if(f.isExpress){
					return true;
				}
			}
			return false;
		}
		
		@Override
		public String invoke(MacroResolver macroResolver) {
			if(subFragment.size()==0){
				return Strings.BLANK;
			}
			StringBand str=new StringBand();
			for(AbstractFragment f:subFragment){
				str.append(f.invoke(macroResolver));
			}
			if(isExpress){
				return invokeExpress(macroResolver, str.toString());
			}
			return str.toString();
		}
		
		void addSub(AbstractFragment f){
			this.subFragment.add(f);
		}

		@Override
		public String toString() {
			return isExpress?(macroStart+StringUtils.join(subFragment,Strings.BLANK)+macroEnd)
					:StringUtils.join(subFragment,Strings.BLANK);
		}
	}
	
	class ParseFragment extends AbstractFragment {
		/**文本*/
		String text;
		
		ParseFragment(String text){
			this.text=text;
		}
		
		ParseFragment(String text,boolean isExpress){
			this.text=text;
			this.isExpress=isExpress;
		}
		
		public String invoke(MacroResolver macroResolver){
			if(isExpress){
				return invokeExpress(macroResolver,text);
			}else{
				return text;
			}
		}

		@Override
		public String toString() {
			return isExpress?(macroStart+text+macroEnd):text;
		}

		@Override
		public boolean hasExpress() {
			return this.isExpress;
		}
	}
}