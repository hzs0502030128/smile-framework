package org.smile.db.handler;

import org.smile.util.CharUtils;
/**
 * 驼峰式的字段与属性的转换
 * @author 胡真山
 *
 */
public class HumpKeyColumnSwaper implements KeyColumnSwaper{
	
	public static KeyColumnSwaper instance=new HumpKeyColumnSwaper();
	
	protected char split='_';

	@Override
	public String columnToKey(String column) {
		char[] chars=column.toCharArray();
		char[] resChars=new char[chars.length];
		int count=0;
		boolean needUp=false;
		for(int i=0;i<chars.length;i++) {
			char c=chars[i];
			if(c==split) {
				needUp=true;
			}else if(needUp){
				needUp=false;
				resChars[count++]=CharUtils.toUpperAscii(c);
			}else {
				resChars[count++]=c;
			}
		}
		return new String(resChars,0,count);
	}

	@Override
	public String KeyToColumn(String key) {
		int len=key.length();
		char[] array=new char[(int)(len<<1)];
		int srcBegin=0;
		int dstBegin=0;
		boolean isUpper=false;
		int flagCount=0;
		for(int i=1;i<len;i++){
			char c=key.charAt(i);
			boolean b=isUpperChar(c);
			if(!isUpper&&b){
				key.getChars(srcBegin, i, array, dstBegin);
				array[dstBegin]=lowerChar(array[dstBegin]);
				srcBegin=i;
				dstBegin=i+flagCount;
				flagCount++;
				array[dstBegin]=split;
				dstBegin++;
			}
			isUpper=b;
		}
		//最后一断
		key.getChars(srcBegin, len, array, dstBegin);
		array[dstBegin]=lowerChar(array[dstBegin]);
		return new String(array, 0, len+flagCount);
	}
	
	/**
	 * 是否是大写字母
	 * @param c
	 * @return
	 */
	private static boolean isUpperChar(char c){
		return 'A'<=c && c<='Z';
	}
	/**
	 * 转小写
	 * @param c
	 * @return
	 */
	private static char lowerChar(char c) {
		return c<'a'?(char)(c+32):c;
	}

}
