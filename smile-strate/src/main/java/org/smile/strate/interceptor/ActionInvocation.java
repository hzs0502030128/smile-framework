package org.smile.strate.interceptor;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.plugin.MethodInterceptor;
import org.smile.plugin.PluginInvocation;
import org.smile.strate.action.Action;
import org.smile.strate.action.ActionElement;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.util.Objects;
import org.smile.validate.ValidateSupport;
/**
 * 一个action动作
 * @author 胡真山
 * @Date 2016年1月18日
 */
public class ActionInvocation extends PluginInvocation{
	/**
	 * 拦截器集合
	 */
	private LinkedList<MethodInterceptor> interceptors;
	/**
	 * action配置信息封装
	 */
	private ActionElement actionElement;
	
	protected DispatchContext context;
	
	public ActionInvocation(DispatchContext context,Method method, Object[] args) {
		super(context.getAction(), method, args);
		this.context=context;
	}
	/**
	 * @param interceptors 添加的拦截器列表
	 * @param target 拦截目标
	 * @param method 拦截方法
	 */
	public ActionInvocation(List<MethodInterceptor> interceptors,DispatchContext context,Method method){
		this(context,method, null);
		if(CollectionUtils.notEmpty(interceptors)){
			this.interceptors=new LinkedList<MethodInterceptor>(interceptors);
		}
	}

	public ActionElement getActionElement(){
		return actionElement;
	}
	/**
	 * 把 action 信息设置进来 
	 * 方便在拦截器中使用
	 * @param actionElement
	 */
	public void setActionElement(ActionElement actionElement) {
		this.actionElement = actionElement;
	}

	@Override
	public <T> T proceed() throws Throwable {
		if(CollectionUtils.notEmpty(interceptors)){
			MethodInterceptor interceptor=interceptors.poll();
			return (T)interceptor.intercept(this);
		}else{
			return (T)doService();
		}
	}
	/**
	 * 处理 action服务
	 * @return
	 * @throws Exception
	 */
	private Object doService() throws Throwable{
		if(!doValidate()){//验证不通过时
			return Action.INPUT;
		}
		//处理业务方法
		Object result=super.proceed();
		doAfter();
		return result;
	}
	/**
	 * 处理验证方法
	 * @return
	 * @throws Exception
	 */
	private boolean doValidate() throws Exception{
		//注解配置验证
		Boolean validate=actionElement.validate(getValidateTarget());
		if(!validate){
			return false;
		}
		// 全局validate方法
		validate = getTarget().validate();
		if(!validate){
			return false;
		}
		//前缀验证方法
		validate =doValidatePrefixMethod();
		return validate;
	}
	/**
	 * 执行以validate为前缀的验证方法
	 * @return
	 */
	private boolean doValidatePrefixMethod() throws Exception{
		//方法名前加validate 验证当前执行的方法
		Method validateMethod=actionElement.getValidateMethod();
		if (validateMethod != null) {
			Class[] validateArgs=validateMethod.getParameterTypes();
			Object[] params=Objects.EMPTY_ARRAY;
			if(ArrayUtils.notEmpty(validateArgs)) {//把action业务方法的参数复制到validate方法中
				params=new Object[validateArgs.length];
				System.arraycopy(this.args, 0, params, 0, params.length);
			}
			return  (Boolean) validateMethod.invoke(target,params);
		}
		return true;
	}
	/**
	 * 处理后置方法
	 * @throws Exception
	 */
	private void doAfter() throws Exception{
		Method afterMethod = this.actionElement.getAfterMethod();
		if (afterMethod != null) {
			afterMethod.invoke(target);
		}
	}
	/**
	 * 方法验证的对象
	 * @return
	 */
	protected ValidateSupport getValidateTarget(){
		return getAction();
	}

	@Override
	public Action getTarget() {
		return getAction();
	}
	/**
	 * 获取当前调用者action对象
	 * @return
	 */
	public Action getAction(){
		return (Action)target;
	}
	/**
	 * 当前action的地址
	 * @return
	 */
	public String getActionURI() {
		return this.context.getUri();
	}
	
	/**
	 * action 类型
	 * @return
	 */
	public Class<? extends Action> getActionClass(){
		return ((Action)this.target).getClass();
	}
	
}
