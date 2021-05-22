package org.smile.strate.interceptor;

import java.util.List;
import java.util.Locale;

import org.smile.beans.MapBean;
import org.smile.beans.converter.BeanException;
import org.smile.http.RequestUtils;
import org.smile.plugin.MethodInterceptor;
import org.smile.reflect.MethodArgs;
import org.smile.strate.action.Action;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.form.ActionParamBeanClass;
import org.smile.strate.form.ActionParameterFiller;
import org.smile.validate.ValidateSupport;

public class MethodActionInvocation extends ActionInvocation{
	
	protected MapBean<ActionParamBeanClass> methodBean;
	
	protected MethodValidateTarget validateTarget;
	
	public MethodActionInvocation(List<MethodInterceptor> interceptors,DispatchContext context,MapBean<ActionParamBeanClass> paramBean) {
		super(interceptors,context,paramBean.getMapBeanClass().getMethod());
		this.methodBean=paramBean;
		updateArgs();
	}
	
	public MapBean<ActionParamBeanClass> getMethodBean() {
		return methodBean;
	}
	
	public void setMethodBean(MapBean<ActionParamBeanClass> methodBean) {
		this.methodBean = methodBean;
	}
	
	public void setMethodBeanProperty(String field,Object value) throws BeanException{
		ActionParameterFiller.handler.setExpFieldValue(methodBean, RequestUtils.convertArrayKeyToProperty(field), value);
	}
	
	/**更新参数从方法对象中*/
	public void updateArgs(){
		MethodArgs args=methodBean.getMapBeanClass().getMethodArgs();
		setArgs(methodBean.getPropertyValues(args.getParamNames()));
	}

	@Override
	protected ValidateSupport getValidateTarget() {
		if(validateTarget==null){
			validateTarget=new MethodValidateTarget();
		}
		return validateTarget;
	}
	
	
	class MethodValidateTarget implements ValidateSupport{

		@Override
		public Locale locale() {
			Action action=getAction();
			return action.locale();
		}

		@Override
		public void addValidateError(String name, String msg) {
			getAction().addValidateError(name, msg);
		}

		@Override
		public Object getTarget() {
			return methodBean;
		}
		
	}
	
}
