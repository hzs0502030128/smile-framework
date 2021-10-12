package org.smile.barcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;

import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.util.StringUtils;
/**
 * 用于折分编码片断
 * @author 胡真山
 * 2015年9月17日
 */
public class SplitTokener {
	/**C最小片断*/
	protected static final int MIN_C_SPLIT_SIZE=4;
	private int character;
	private boolean eof;
	private int index;
	private int line;
	/****/
	private Stack<Character> previous;
	private final Reader reader;
	private boolean usePrevious;

	
	public SplitTokener(Reader reader) {
		this.reader = reader.markSupported() ? reader : new BufferedReader(reader);
		this.eof = false;
		this.usePrevious = false;
		this.previous = new Stack<Character>();
		this.index = 0;
		this.character = 1;
		this.line = 1;
	}

	public SplitTokener(String s) {
		this(new StringReader(s));
	}

	
	public void back() throws BarcodeException {
		if (this.index <= 0) {
			throw new BarcodeException("Stepping back two steps is not supported");
		}
		this.index -= 1;
		this.character -= 1;
		this.usePrevious = true;
		this.eof = false;
	}

	public boolean end() {
		return this.eof && !this.usePrevious;
	}

	public void reset(){
		try {
			this.reader.reset();
			this.previous.pop();
			index--;
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}
	/**
	 * 判断是否还有更多字符
	 * @return
	 * @throws BarcodeException
	 */
	public boolean more() throws BarcodeException {
		this.next();
		if (this.end()) {
			return false;
		}
		this.back();
		return true;
	}

	/**下一个字符*/
	public char next() throws BarcodeException {
		int c;
		if (this.usePrevious) {
			this.usePrevious = false;
			c = this.previous.pop();
		} else {
			try {
				this.reader.mark(index);
				c = this.reader.read();
			} catch (IOException exception) {
				throw new BarcodeException(exception);
			}

			if (c <= 0) { 
				this.eof = true;
				c = 0;
			}
		}
		this.index += 1;
		
		this.character += 1;
		char indexChar=(char) c;
		this.previous.push(indexChar);
		return indexChar;
	}

	
	public char next(char c) throws BarcodeException {
		char n = this.next();
		if (n != c) {
			throw this.parseError("Expected '" + c + "' and instead saw '" + n
					+ "'");
		}
		return n;
	}

	public String next(int n) throws BarcodeException {
		if (n == 0) {
			return Strings.BLANK;
		}

		char[] chars = new char[n];
		int pos = 0;

		while (pos < n) {
			chars[pos] = this.next();
			if (this.end()) {
				throw this.parseError("Substring bounds error");
			}
			pos += 1;
		}
		return new String(chars);
	}
	
	/***
	 * 查找出一个片断字符串
	 * @return
	 * @throws BarcodeException
	 */
	public String nextString() throws BarcodeException{
		StringBuffer sb=new StringBuffer();
		char c;
		Boolean isnumber=null;
		for(;;){
			c=this.next();
			if(end()){
				if(isnumber!=null&&isnumber){
					//如果是数字类型结束拆分出一个偶数个字符的片断
					if(sb.length()>MIN_C_SPLIT_SIZE){
						if(sb.length()%2!=0){
							back();
							reset();
							return sb.substring(0,sb.length()-1);
						}
					}
					return sb.toString();
				}else{
					return sb.toString();
				}
			}
			if(isnumber==null){
				//第一个字符直接添加
				isnumber=StringUtils.isNumberChar(c);
				sb.append(c);
			}else{
				boolean nextIsNumber=StringUtils.isNumberChar(c);
				if(isnumber!=nextIsNumber){
					if(isnumber){
						//遇到下一个字符已经不是数字了 C 编码只有在大于4的时候才有意义  因为要添加一个转换符
						if(sb.length()>=MIN_C_SPLIT_SIZE){
							//如果前是偶数个完成一个片断
							if(sb.length()%2==0){
								back();
								return sb.toString();
							}else{
								//如果是奇数 退回偶数个字符
								back();
								//重读
								reset();
								return sb.substring(0,sb.length()-1);
							}
						}else {
							sb.append(c);
							isnumber=nextIsNumber;
						}
					}else{
						back();
						return sb.toString();
					}
				}else{
					//类型一个继续添加
					sb.append(c);
				}
			}
		}
	}
	
	public BarcodeException parseError(String message) {
		return new BarcodeException(message + this.toString());
	}

	
	public String toString() {
		return " at " + this.index + " [character " + this.character + " line "
				+ this.line + "]";
	}


	public BarcodeException syntaxError(String string) throws BarcodeException {
		return new BarcodeException(string);
	}
}
