package org.smile.barcode;
/**
 * 编码片断 一个条码可能存在多种编码   
 * 以片断形式拆分
 * @author 胡真山
 * 2015年9月17日
 */
public class CodeSplit {
	/**
	 * 片断字符串文本
	 */
	protected String text;
	/**
	 * 片断编码类型
	 */
	protected char type;
	
	public CodeSplit(char type,String text){
		this.text=text;
		this.type=type;
	}
	
	public int  getShift(String start){
		switch(type){
			case Code128.TYPE_A:return Code128.CODE_A;
			case Code128.TYPE_B:return Code128.CODE_B;
			case Code128.TYPE_C:return Code128.CODE_C;
			default:return Code128.CODE_B;
		}
	}
	
	public Integer[] getCodeIndex(){
		if(Code128.TYPE_C==type){
			Integer[] indexs=new Integer[text.length()/2];
			char[] cs=text.toCharArray();
			for(int i=0;i<indexs.length;i++){
				indexs[i]=Integer.valueOf(cs[2*i]+""+cs[2*i+1]);
			}
			return indexs;
		}else{
			Integer[] result=new Integer[text.length()];
			for(int i=0;i<result.length;i++){
				result[i]=(int)text.charAt(i)-32;
			}
			return result;
		}
	}

	public String getText() {
		return text;
	}

	public char getType() {
		return type;
	}
	

	@Override
	public String toString() {
		return type+":"+text;
	}
	/**
	 * 片断的长度  指的是一个code值为一个长度
	 * @return
	 */
	public int getLength(){
		int len=0;
		switch(type){
			case Code128.TYPE_A:
			case Code128.TYPE_B:
				return len+text.length();
			case Code128.TYPE_C:
				return len+text.length()/2;
		}
		return len;
	}
	
}
