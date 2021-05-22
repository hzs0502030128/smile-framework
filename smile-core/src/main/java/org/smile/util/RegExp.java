package org.smile.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式类
 * @author strive
 *
 */
public class RegExp {
	/**默认的字符串拆分  [,;\r\n]  */
	public static final RegExp DEF_STR_SPLIT = new RegExp("[,;\r\n\t]+");
	
	private Pattern p;
	/**保存hashcode*/
	private volatile int hashCode=-1;
	
	/**
	 * 构造方法
	 * @param reg 正则表达式规则
	 */
	public RegExp(String reg) {
		this.p = Pattern.compile(reg);
	}

	public String getPattern() {
		return p.pattern();
	}

	/**
	 * 构造方法
	 * @param reg 正则表达式规则
	 * @param lowerCase 是否区分大小写  false时 不区分
	 */
	public RegExp(String reg, boolean lowerCase) {
		if (lowerCase == false) {
			this.p = Pattern.compile(reg, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		} else {
			this.p = Pattern.compile(reg);
		}
	}
	
	/**
	 * 生成hashCode 
	 */
	private int generatorHashCode() {
		int result=HashCode.hash(HashCode.SEED,this.getPattern().hashCode());
		result=HashCode.hash(result, this.p.flags());
		return result;
	}

	/**
	 * 从一个字符串中匹配查找  
	 *    如:new RegExp("[a-b]+").find("13fgb45d45");
	 *    结果：["fgb","d"]
	 * @param s
	 * @return 匹配规则的 子字符串数组
	 */
	public String[] find(String s) {
		List<String> list = new ArrayList<String>();
		Matcher m = p.matcher(s);
		while (m.find()) {
			list.add(m.group());
		}
		return convert(list);
	}

	/**
	 * 查找第一个匹配
	 * @param s
	 * @return 匹配的子字符串
	 */
	public String first(String s) {
		Matcher m = p.matcher(s);
		while (m.find()) {
			return m.group();
		}
		return null;
	}

	/**
	 * 第一个匹配的索引
	 * @param s
	 * @return
	 */
	public int firstIndex(String s) {
		Matcher m = p.matcher(s);
		int index = -1;
		while (m.find()) {
			return m.start();
		}
		return index;
	}
	/**
	 * 查找位置
	 * @param s
	 * @param index
	 * @return
	 */
	public int search(String s,int index){
		Matcher m = p.matcher(s);
		if(m.find(index)) {
			return m.start();
		}
		return -1;
	}

	/**
	 * 查找匹配信息
	 * @param m
	 * @param index
	 * @return
	 */
	public MatchInfo firstMatch(Matcher m, int index) {
		while (m.find(index)) {
			MatchInfo info = new MatchInfo();
			info.start = m.start();
			info.end = m.end();
			info.context = m.group();
			return info;
		}
		return null;
	}

	/**
	 * 第一个匹配结束的索引位置
	 * @param s
	 * @return
	 */
	public int firstIndexEnd(String s) {
		Matcher m = p.matcher(s);
		int index = -1;
		while (m.find()) {
			return m.end();
		}
		return index;
	}

	/**
	 * 以正则表达式分割一个字符串为一个数组
	 * 分割出来的数组中为空的被去除 只保存不为空的
	 * 如: new RegExp("b").split("erbbhbq");
	 *     的结果为:["er","h","q"]
	 * @param s
	 * @return
	 */
	public String[] split(String s) {
		return p.split(s);
	}
	/**
	 * 
	 * @param s
	 * @param discardBlack 是否去掉空格
	 * @return
	 */
	public String[] split(String s,boolean discardBlack){
		if(discardBlack){
			List<String> list=splitAndTrimNoBlack(s, new ArrayList<String>());
			return list.toArray(new String[list.size()]);
		}else{
			return p.split(s);
		}
	}
	/**
	 * 拆分后并且去空格后的结果
	 * 如果为 "" 将不返回在结果中
	 * @param s
	 * @return
	 */
	public LinkedList<String> splitAndTrimNoBlack(String s){
		LinkedList<String> list=new LinkedList<String>();
		splitAndTrimNoBlack(s, list);
		return list;
	}
	/**
	 * 拆分后的空格去除
	 * 拆分成一个list
	 * @param s
	 * @return
	 */
	public List<String> splitToList(String s) {
		return splitToList(s, new LinkedList<String>());
	}
	
	/**
	 * 分隔后 去空格,空字符串不返回
	 * @param s
	 * @param list
	 * @return
	 */
	public List<String> splitAndTrimNoBlack(String s,List<String> list){
		String[] strs = p.split(s);
		for (String str : strs) {
			str=StringUtils.trim(str);
			if(str.length()>0){
				list.add(str);
			}
		}
		return list;
	}
	/**
	 * 拆分到一个列表中
	 * @param s
	 * @param list
	 * @return
	 */
	public List<String> splitToList(String s,List<String> list){
		String[] strs = p.split(s);
		for (String str : strs) {
			list.add(str);
		}
		return list;
	}
	/**
	 * 拆分成一个set
	 * @param s
	 * @return
	 */
	public Set<String> splitToSet(String s,Set<String> set){
		String[] strs = p.split(s);
		for (String str : strs) {
			set.add(str);
		}
		return set;
	}
	
	public Set<String> splitToSet(String s){
		return splitToSet(s, new LinkedHashSet<String>());
	}
	

	/**
	 * 替换一个字符串
	 * @param resurceStr 被替换的目标字符串
	 * @param newStr 要替换成
	 * @return 新字符串
	 */
	public String replaceAll(String resurceStr, String newStr) {
		Matcher matcher = matcher(resurceStr);
		return matcher.replaceAll(newStr);
	}

	/**
	 * 得到正则表达式字符串
	 * @return
	 */
	public String getFormatStr() {
		return this.p.pattern();
	}

	/**
	 * 是否存在可匹配
	 * @param s
	 * @return
	 */
	public boolean test(String s) {
		Matcher m = p.matcher(s);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}
	/***
	 * 是否完全匹配
	 * @param s
	 * @return
	 */
	public boolean matches(String s){
		Matcher m = p.matcher(s);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回 创建的匹配器
	 * @param s
	 * @return
	 */
	public Matcher matcher(String s) {
		return p.matcher(s);
	}

	/**
	 * 最后一次匹配的索引
	 * @param s
	 * @return
	 */
	public int lastIndex(String s) {
		Matcher m = p.matcher(s);
		int index = -1;
		while (m.find()) {
			index = m.start();
		}
		return index;
	}

	/**
	 * 最后一次匹配的索引
	 * @param s
	 * @return
	 */
	public MatchInfo lastMate(String s) {
		Matcher m = p.matcher(s);
		MatchInfo info = null;
		while (m.find()) {
			if (info == null) {
				info = new MatchInfo();
			}
			info.start = m.start();
			info.context = m.group();
			info.end = m.end();
		}
		return info;
	}

	/**
	 * 第一次匹配的索引
	 * @param s
	 * @return
	 */
	public MatchInfo firstMatch(String s) {
		Matcher m = p.matcher(s);
		MatchInfo info = null;
		if (m.find()) {
			info = new MatchInfo();
			info.start = m.start();
			info.context = m.group();
			info.end = m.end();
		}
		return info;
	}

	/**
	 * 最后一次匹配的字符串后后面的索引
	 * @param s
	 * @return
	 */
	public int lastIndexEnd(String s) {
		Matcher m = p.matcher(s);
		int index = -1;
		while (m.find()) {
			index = m.end();
		}
		return index;
	}

	/**
	 * 检索出所有的匹配的内容
	 * @param s
	 * @return
	 */
	public List<MatchInfo> findAll(String s) {
		Matcher m = p.matcher(s);
		List<MatchInfo> list = new ArrayList<MatchInfo>();
		int index = 0;
		int count=0;
		MatchInfo last=null;
		MatchInfo previous=null;
		while (m.find(index)) {
			last = new MatchInfo();
			last.start = m.start();
			last.end = m.end();
			last.context = m.group();
			last.index=count;
			index = m.end();
			list.add(last);
			if(previous!=null){
				//记录上一个的下一个是当前一个
				previous.next=last;
				//当前的记录的上一个
				last.previous=previous;
			}
			previous=last;
			count++;
		}
		return list;
	}
	
	/**
	 * 检索出所有的匹配的内容
	 * @param s
	 * @return
	 */
	public MatchInfo matchAll(String s) {
		Matcher m = p.matcher(s);
		int index = 0;
		int count=0;
		MatchInfo frist=null;
		MatchInfo last=null;
		MatchInfo previous=null;
		while (m.find(index)) {
			last = new MatchInfo();
			if(frist==null){//记录第一个
				frist=last;
			}
			last.start = m.start();
			last.end = m.end();
			last.context = m.group();
			last.index=count;
			index = m.end();
			if(previous!=null){
				//记录上一个的下一个是当前一个
				previous.next=last;
				//当前的记录的上一个
				last.previous=previous;
			}
			previous=last;
			count++;
		}
		return frist;
	}
	/**
	 * 创建
	 * @param s
	 * @return
	 */
	public RegExpTokenizer tokenizer(String s){
		MatchInfo info=matchAll(s);
		if(info!=null){
			return new RegExpTokenizer(s,info);
		}else{
			return new RegExpTokenizer(s);
		}
	}

	/**
	 * 最后匹配的子字符串
	 * @param s
	 * @return
	 */
	public String last(String s) {
		Matcher m = p.matcher(s);
		String result = null;
		while (m.find()) {
			result = m.group();
		}
		return result;
	}

	/**
	 * 把集合转为字符串数组
	 * @param list
	 * @return
	 */
	private String[] convert(List<String> list) {
		int i = 0, size = list.size();
		if (size > 0) {
			String[] strs = new String[size];
			for (i = 0; i < size; i++) {
				strs[i] = list.get(i).toString();
			}
			return strs;
		} else {
			return null;
		}
	}
	
	public class RegExpTokenizer extends StringTokenizer{
		/**匹配信息*/
		private MatchInfo first;
		/**当前的匹配*/
		private MatchInfo current;
		/**是否已结束*/
		private boolean eof=false;
		/**记录标记*/
		private MatchInfo mark;
		/**
		 * @param text
		 * @param frist
		 */
		protected RegExpTokenizer(String text,MatchInfo frist){
			this.text=text;
			this.first=frist;
		}
		
		private RegExpTokenizer(String text){
			this.text=text;
		}
		
		@Override
		public boolean hasMoreElements() {
			return hasMoreTokens();
		}

		@Override
		public String nextElement() {
			return nextToken();
		}

		@Override
		public boolean hasMoreTokens() {
			return eof==false;
		}

		@Override
		public String nextToken() {
			String token;
			if(current==null){
				if(first!=null){
					token= text.substring(0,first.start);
					current=first;
				}else{
					//没有匹配的时候只有一个
					this.eof=true;
					token= text;
				}
			}else{
				if(current.next==null){
					token=text.substring(current.end,text.length());
					//最后一个了
					this.eof=true;
				}else{
					token= text.substring(current.end, current.next.start);
					this.current=current.next;
				}
			}
			return token;
		}
		
		@Override
		public void mark() {
			this.mark=current;
		}
		@Override
		public void reset() {
			this.current=this.mark;
			this.eof=false;
		}

		@Override
		public String separator() {
			if(this.current==null){
				return null;
			}
			return this.current.context;
		}

		@Override
		public StringTokenizer skipBlank() {
			if(first!=null){
				MatchInfo info=current;
				if(info==null){
					info=first;
				}
				//与上一个分隔的间距为0
				while((info.index==0&&info.start==0)||(info.start==info.previous.end)){
					this.current=info;
					info=info.next;
				}
			}
			return this;
		}

		@Override
		public String nextToken(String separator) {
			MatchInfo info=this.current;
			if(info==null){
				info=this.first;
			}
			while(info.next!=null){
				if(info.context.equals(separator)){
					return info.subStrBetweenPrevious(this.text);
				}
				info=info.next;
			}
			return null;
		}
		
	}

	public class MatchInfo{
		/**内容开始索引*/
		protected int start;
		/**结束的索引*/
		protected int end;
		/**匹配到的内容*/
		protected String context;
		/**匹配个数的索引*/
		protected int index;
		/**下一个匹配信息*/
		protected MatchInfo next;
		/**上一个*/
		protected MatchInfo previous;

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

		public String getContext() {
			return context;
		}

		public int getIndex() {
			return index;
		}

		/**
		 * 是否最后一个匹配
		 * @return
		 */
		public boolean isLast() {
			return this.next==null;
		}
		/**
		 * 是否第一个匹配
		 * @return
		 */
		public boolean isFirst(){
			return this.index==0;
		}

		/**
		 * 下一个匹配
		 * @return
		 */
		public MatchInfo getNext() {
			return next;
		}

		@Override
		public String toString() {
			return index+"("+start+","+end+")["+context+"]";
		}
		/**
		 * 与下一个匹配之间的子串
		 * @param str
		 * @return
		 */
		public String subStrBetweenNext(String str){
			if(next==null){
				return str.substring(end,str.length());
			}else{
				return str.substring(end, next.start);
			}
		}
		/**
		 * 与上一个匹配之间的子串
		 * @param str
		 * @return
		 */
		public String subStrBetweenPrevious(String str){
			if(index==0){//是第一个
				return str.substring(0,start);
			}else{
				return str.substring(previous.end, start);
			}
		}
	}

	@Override
	public int hashCode() {
		if(this.hashCode==-1){
			this.hashCode=generatorHashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RegExp){
			RegExp exp=(RegExp)obj;
			return getPattern().equals(exp.getPattern())&&p.flags()==exp.p.flags();
		}
		return false;
	}
	
}
