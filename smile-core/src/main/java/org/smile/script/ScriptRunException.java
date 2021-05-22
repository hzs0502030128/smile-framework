package org.smile.script;
/***
 * 脚本 代码 执行异常
 * @author 胡真山
 * 2015年11月16日
 */
public class ScriptRunException extends RuntimeException{
	public ScriptRunException(Throwable e){
		super("script execute error ",e);
	}
	
	public ScriptRunException(String msg,Throwable e){
		super("script execute error: "+msg,e);
	}
}
