package org.smile.report.function;


/**
 * 是否对ognl进行支持
 * @author 胡真山
 */
public class OgnlSupport {
	
	private static Boolean support;
	
	private static IFunction ognlFucton;
	/**
	 * ognl表达式支持函数
	 * @return
	 */
	public static IFunction getOgnlFunction(){
		if(ognlFucton==null&&isSupport()){
			try{
				ognlFucton=new OgnlFunction();
			}catch(Exception e){
				support=false;
			}
		}
		return ognlFucton;
	}
	/**
	 * 是否存在对ognl表达式支持
	 * @return
	 */
	public static  boolean isSupport(){
		if(support==null){
			Class ognlClass=null;
			try {
				ognlClass = Class.forName("ognl.Ognl");
			} catch (ClassNotFoundException e) {}
			if(ognlClass!=null){
				support=true;
			}else{
				support=false;
			}
		}
		return support;
	} 
}
