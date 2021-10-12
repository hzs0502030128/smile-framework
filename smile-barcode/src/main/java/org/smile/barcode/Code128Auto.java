package org.smile.barcode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.commons.SmileRunException;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;

/**
 * 一种相对合理的组合型128条形码
 * @author 胡真山
 * 2015年10月28日
 */
public class Code128Auto extends Code128 {
	/**起始标记*/
	protected String start;
	/**分段的字段串*/
	protected LinkedList<String> splits=new LinkedList<String>();
	/**有没有小写字母*/
	protected static  RegExp lowerReg=new RegExp("[a-z]+");
	/**编码片段*/
	protected LinkedList<CodeSplit> codeSplits=new LinkedList<CodeSplit>();
	/**各种类型条数*/
	protected Map<Character,Integer> typeCount=new HashMap<Character,Integer>();
	
	/**
	 * 自动配置出一选种较优的方案
	 * @param text
	 */
	public Code128Auto(String text) {
		super(text);
		try {
			SplitTokener token=new SplitTokener(text);
			while(!token.end()){
				splits.add(token.nextString());
			}
		} catch (BarcodeException e) {
			throw new SmileRunException(e);
		}
		checkLastString();
		Iterator<String> iterator=splits.iterator();
		addCodeSplit(null, iterator);
		checkCodeSplits();
		createBarcode();
	}
	/**检查最后一位是数字的情况*/
	protected void checkLastString(){
		int len=splits.size();
		if(len>=3){//当大于3个片段的时候才有优化空间
			String str=splits.getLast();
			if(str.length()==1){
				if(StringUtils.isNumberChar(str.charAt(0))){
					String number=splits.get(len-2);
					if(isNumberSplit(number)){//对明后一个片断是数据的请情况进行优化
						String first=splits.get(len-3);
						//把最后一个添加到数片段中做为C编码  
						splits.set(len-3, first+number.charAt(0));
						//把第一个数字移到前一个片段中
						splits.set(len-2, number.substring(1)+str);
						splits.removeLast();
					}
				}
			}
		}
	}
	/**
	 * 检查出最优方案
	 */
	protected void checkCodeSplits(){
		CodeSplit first=codeSplits.getFirst();
		if(first.type==TYPE_C){
			start=C_START;
		}else if(first.type==TYPE_B){
			start=B_START;
		}else if(lowerReg.test(first.text)){
			start=B_START;
		}else{
			start=A_START;
		}
	}
	/**
	 * 添加一个分段 并记录每个分段类型个数统计
	 * @param type
	 * @param text
	 */
	protected void addOneCodeSplit(char type,String text){
		codeSplits.add(new CodeSplit(type,text));
		Integer value=typeCount.get(type);
		if(value==null){
			typeCount.put(type, 1);
		}else{
			typeCount.put(type, value+1);
		}
	}
	
	/***
	 * 添加分段
	 * @param temp
	 * @param iterator
	 */
	protected void addCodeSplit(String temp,Iterator<String> iterator){
		if(iterator.hasNext()){
			if(temp==null){
				temp=iterator.next();
			}
			if(isNumberSplit(temp)){
				addOneCodeSplit(TYPE_C, temp);
				addCodeSplit(null, iterator);
			}else if(iterator.hasNext()){
				String tempNext=iterator.next();
				if(isNumberSplit(tempNext)){
					addOneCodeSplit(TYPE_B, temp);
					addOneCodeSplit(TYPE_C, tempNext);
					addCodeSplit(null, iterator);
				}else{
					addCodeSplit(temp+tempNext, iterator);
				}
			}else{
				addCodeSplit(temp, iterator);
			}
		}else if(temp!=null){
			addOneCodeSplit(TYPE_B, temp);
		}
	}
	/***
	 * 是不是数字分段 数字分段用C类型来解析
	 * @param s
	 * @return
	 */
	protected boolean isNumberSplit(String s){
		int len=s.length();
		if(len%2!=0||len<SplitTokener.MIN_C_SPLIT_SIZE){//数字片段都是4个字符以上的
			return false;
		}
		for(int i=0;i<len;i++){
			if(!StringUtils.isNumberChar(s.charAt(i))){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<CodeSplit> getSplit(){
		return codeSplits;
	}
	
	protected int getCheckValue(){
		if(start==C_START){
			return CHECK_C;
		}else if(start==B_START){
			return CHECK_B;
		}else{
			return CHECK_A;
		}
	}
	
	@Override
	protected String getStart() {
		return start;
	}
}
