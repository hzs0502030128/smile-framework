package org.smile.plugin;

import java.lang.reflect.Method;
/**
 * 被拦截的动作调用
 * @author 胡真山
 * 2015年9月25日
 */
public class PluginInvocation implements Invocation{
	/**
	 * 调用的方法
	 */
	protected Method method;
	/**
	 * 调用的参数
	 */
	protected Object[] args;
	/**
	 * 调用的对象
	 */
	protected Object target;
	/**
	 * 调用的结果
	 */
	protected Object result;
	
	public PluginInvocation(Object target,Method method,Object[] args){
		this.target=target;
		this.method=method;
		this.args=args;
	}
	public Method getMethod() {
		return method;
	}
	
	public Object[] getArgs() {
		return args;
	}
	
	public <T> T getTarget() {
		return (T)target;
	}
	
	/**
	 * 设置调用的参数
	 * @param args
	 */
	public void setArgs(Object[] args){
		this.args=args;
	}
	/**
	 * 动作处理
	 * @return
	 * @throws Exception
	 * @throws Throwable 
	 */
	public <T> T proceed() throws Throwable {
		result=this.method.invoke(this.target, this.args);
		return (T)result;
	}
	/**
	 * 返回调用的结果
	 * @return
	 */
	public Object getResult() {
		return result;
	}
	/**
	 * 方法的返回类型
	 * @return
	 */
	public Class getReturnType(){
		return method.getReturnType();
	}
	
	@Override
	public String toString(){
		return target+" invoke method "+method+" use "+args;
	}
	
}
