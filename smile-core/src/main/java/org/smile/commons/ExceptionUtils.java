package org.smile.commons;

import java.io.PrintWriter;

import org.smile.Smile;
import org.smile.io.StringWriter;
import org.smile.template.SimpleStringTemplate;
import org.smile.template.Template;
/**
 * 对异常操作的工具类
 * @author 胡真山
 *
 */
public class ExceptionUtils {
	/**异常信息显示模板*/
	protected static Template EXCEPTION_TEMPLATE=new SimpleStringTemplate("(${className}.java:${lineNumber}).${methodName} - ${exceptionClass} ${message}");
	/**
	 * 循环出异常的message
	 * @param e
	 * @return
	 */
	public static String getExceptionMsg(Throwable e){
		StringBand msg=new StringBand(50);
		do{
			msg.append(e.getClass().getSimpleName()).append(":").append(e.getMessage()).append(Smile.LINE_SEPARATOR);
		}while((e=e.getCause())!=null);
		return msg.toString();
	}
	/**
	 *	 获取异常的消息 循环cause by
	 * @param e
	 * @param split 对多个异常消息指定一个分隔符
	 * @return
	 */
	public static String getMessage(Throwable e,String split) {
		StringBand msg=new StringBand(50);
		boolean first=true;//是否是一个
		do{
			if(first) {
				first=false;
			}else {
				msg.append(split);
			}
			if(e.getMessage()!=null) {
				msg.append(e.getMessage());
			}
		}while((e=e.getCause())!=null);
		return msg.toString();
	}
	/**
	 * 获取异常的message会循环异常的cause
	 * @param e
	 * @return
	 */
	public static String getMessage(Throwable e) {
		return getMessage(e, Smile.LINE_SEPARATOR);
	}
	
	
	/**
	 * 异常发生的行信息
	 * @param e
	 * @return
	 */
	protected static void appendMsgInfo(StringBuilder infos,Throwable e){
		ExceptionMsgInfo info=getMsgInfo(e);
		if(info!=null){
			infos.append(EXCEPTION_TEMPLATE.processToString(info));
		}
	}
	/**
	 * 获取一个异常信息的内容
	 * @param e
	 * @return
	 */
	protected static ExceptionMsgInfo getMsgInfo(Throwable e){
		StackTraceElement[] stackTrace = e.getStackTrace();
		if(stackTrace.length>0){
			StackTraceElement stackTraceElement=stackTrace[0];
			String className= stackTraceElement.getClassName();
			String methodName=stackTraceElement.getMethodName();
			ExceptionMsgInfo info= new ExceptionMsgInfo(className,methodName,stackTraceElement.getLineNumber(),e.getMessage());
			info.exceptionClass=e.getClass().getName();
			return info;
		}
		return null;
	}
	/**
	 * 获取异常发生的详细信息
	 * @param e
	 * @return
	 */
	public static String getExceptionMsgDetail(Throwable e){
		StringBuilder msg=new StringBuilder(100);
		do{
			appendMsgInfo(msg, e);
			msg.append(Smile.LINE_SEPARATOR);
		}while((e=e.getCause())!=null);
		return msg.toString();
	}
	/**
	 * 转成字符串信息
	 * @param e
	 * @return
	 */
	public static String throwableStack(Throwable e){
		StringWriter sw = new StringWriter();  
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	/**
	 * 转成字符串
	 * @param e 
	 * @param maxSize 返回字符串的最大长度
	 * @return
	 */
	public static String throwableStack(Throwable e,int maxSize){
		StringWriter sw = new StringWriter();  
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		int size=sw.size();
		if(size>maxSize){
			return sw.toString(size-maxSize,maxSize);
		}
		return sw.toString();
	}
	/**
	 * 查询最终的异常起因
	 * @param e 如果没有cause返回本身
	 * @return
	 */
	public static Throwable getRootCause(Throwable e){
		Throwable cause=e;
		while(cause.getCause()!=null){
			cause=cause.getCause();
		}
		return cause;
	}
	/**
	 * 是否存在异常起因
	 * @param e
	 * @param clazz 要判断的起因类型
	 * @return
	 */
	public static boolean hasCause(Throwable e,Class<? extends Throwable> clazz){
		Throwable cause=e;
		do{
			if(clazz.isAssignableFrom(e.getClass())){
				return true;
			}
		}while((cause=cause.getCause())!=null);
		return false;
	}
	
	public static class ExceptionMsgInfo{
		/**发生异常的类名*/
		protected String className;
		/**发生异常的行号*/
		protected int lineNumber;
		/**生发异常的方法*/
		protected String methodName;
		/**异常的信息*/
		protected String message;
		/**异常的类名*/
		protected String exceptionClass;
		
		protected ExceptionMsgInfo(String className,String methodName,int lineNumber,String message){
			this.className=className;
			this.methodName=methodName;
			this.lineNumber=lineNumber;
			this.message=message;
		}
		
		public String getExceptionClass() {
			return exceptionClass;
		}

		public void setExceptionClass(String exceptionClass) {
			this.exceptionClass = exceptionClass;
		}

		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public int getLineNumber() {
			return lineNumber;
		}
		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
		}
		public String getMethodName() {
			return methodName;
		}
		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
