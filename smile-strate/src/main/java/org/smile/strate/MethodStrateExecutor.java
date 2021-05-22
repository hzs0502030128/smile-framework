package org.smile.strate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.smile.annotation.AnnotationUtils;
import org.smile.beans.MapBean;
import org.smile.beans.converter.BeanException;
import org.smile.collection.ArrayUtils;
import org.smile.commons.SmileRunException;
import org.smile.http.RequestUtils;
import org.smile.plugin.MethodInterceptor;
import org.smile.reflect.MethodParamBeanClass;
import org.smile.strate.action.ActionElement;
import org.smile.strate.action.ClassElement;
import org.smile.strate.ann.RequestBody;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.form.ActionFormResult;
import org.smile.strate.form.ActionParamBeanClass;
import org.smile.strate.form.ActionParameterFiller;
import org.smile.strate.interceptor.MethodActionInvocation;

/**
 * strate 跳转执行类
 * 以field属性
 * @author 胡真山
 * @Date 2016年1月18日
 */
public class MethodStrateExecutor extends FieldStrateExecutor{
	/**
	 * action方法 参数信息
	 */
	protected Map<Class,Map<String,ActionParamBeanClass>> actionMethodArgs=new HashMap<Class,Map<String,ActionParamBeanClass>>();

	@Override
	public Object doActionMethod(DispatchContext context,ActionFormResult fillResult) throws StrateException {
		ActionElement actionElement=context.getActionElement();
		HttpServletRequest request=context.getRequest();
		if(fillResult.getMethodParamBean()!=null){
			MapBean<ActionParamBeanClass> paramBean=fillResult.getMethodParamBean();
			if(fillResult.hasDynamicAction()){
				if(!actionElement.getName().equals(fillResult.getDynamicAction())){
					fillResult=new ActionFormResult();
					Enumeration<String> enuma = request.getParameterNames();
					ActionParamBeanClass methodBeanClass=actionMethodArgs.get(actionElement.getActionClass()).get(actionElement.getMethod());
					if(methodBeanClass==null){
						throw new  StrateException("处理action:"+actionElement.getName()+"失败,找不到相应的方法参数封装对象");
					}
					paramBean=methodBeanClass.newInstance();
					fillResult=new ActionFormResult();
					while (enuma.hasMoreElements()){
						String key =enuma.nextElement();
						String[] value= request.getParameterValues(key); 
						if (ArrayUtils.isEmpty(value)) {
							continue;
						}
						try {
							ActionParameterFiller.handler.setExpFieldValue(paramBean, RequestUtils.convertArrayKeyToProperty(key), value);
						} catch (BeanException e) {
							throw new StrateException("设置方法"+methodBeanClass.getMethod()+"的参数值出错"+key+" "+value,e);
						}
					}
					if(methodBeanClass.hasRequestBody()) {
						methodBeanClass.doRequestBody(context.getAction(), paramBean);
					}
					methodBeanClass.doRequestBody(context.getAction(), paramBean);
					fillResult.setMethodParamBean(paramBean);
				}
			}
			// 拦截器处理
			List<MethodInterceptor> interceptors = actionElement.getPackageElement().getInterceptors();
			MethodActionInvocation invocation = new MethodActionInvocation(interceptors,context,paramBean);
			invocation.setActionElement(actionElement);
			try{
				return invocation.proceed();
			}catch(Throwable e){
				throw new StrateException("处理action "+actionElement.getName()+"异常",e);
			}
		}else{
			return super.doActionMethod(context,fillResult);
		}
	}


	@Override
	public void initOnClassElement(ClassElement ce) {
		//action类
		Class clazz=ce.getActionClass();
		Map<String,ActionParamBeanClass> methodArgsMap=actionMethodArgs.get(clazz);
		if(methodArgsMap==null){
			methodArgsMap=new HashMap<String, ActionParamBeanClass>();
			actionMethodArgs.put(clazz, methodArgsMap);
		}
		Method[] methods=clazz.getDeclaredMethods();
		for(Method m:methods){
			if(ce.existsActionByMethodName(m.getName())){
				if(methodArgsMap.containsKey(m.getName())){
					throw new SmileRunException("strate 框架中的方法  action 不支持方法重载:"+m.getName()+","+clazz+" 存在相同的方法名 "+m.getName());
				}
				ActionParamBeanClass beanClass=new ActionParamBeanClass(m);
				requestBodyParamName(beanClass);
				beanClass.initRequestResponseParamName();
				methodArgsMap.put(m.getName(), beanClass);
			}
		}
	}
	
	
	/**
	 * 处理是否存在requestBody参数
	 * @param beanClass
	 */
	private void requestBodyParamName(ActionParamBeanClass beanClass) {
		Method m=beanClass.getMethod();
		//方法上是否存在注解
		RequestBody requestBody=AnnotationUtils.getAnnotation(m, RequestBody.class);
		if(requestBody!=null) {
			//取第一个参数为body参数
			String fieldName=beanClass.getFieldNames().iterator().next();
			beanClass.requestBodyInfo(fieldName, requestBody.requried());
		}else {
			//参数注解
			Annotation[][] parameterAnns=m.getParameterAnnotations();
			if(ArrayUtils.notEmpty(parameterAnns)){
				int index=0;
				for(Annotation[] anns:parameterAnns){
					for(Annotation ann:anns){
						if(ann instanceof RequestBody){
							int i=0;
							for(String fieldName:beanClass.getFieldNames()) {
								if(index==i) {
									beanClass.requestBodyInfo(fieldName,((RequestBody) ann).requried());
									break;	
								}
							}
						}
					}
					index++;
				}
			}
		}
	}

	@Override
	public ActionFormResult requestToAction(DispatchContext context) throws Exception {
		ActionFormResult methodResult=requestToMethodParam(context);
		return methodResult;
	}
	/**
	 * 把request中的参数信息赋值到action方法参数中去
	 * @param request
	 * @param action
	 * @param actionElement action配置信息
	 * @return
	 * @throws Exception
	 */
	protected ActionFormResult requestToMethodParam(DispatchContext context) throws Exception {
		Class clazz=context.getAction().getClass();
		Map<String,ActionParamBeanClass> classMethodArgs=actionMethodArgs.get(clazz);
		if(classMethodArgs!=null){
			String actionMethodName=context.getActionMethodName();
			ActionParamBeanClass methodBeanClass=classMethodArgs.get(actionMethodName);
			return ActionParameterFiller.fillRequestToAction(context,methodBeanClass);
		}
		return null;
	}
	
	@Override
	public Method getActionMethod(Class actionClass, String method) {
		Map<String,ActionParamBeanClass> classMethodArgs=actionMethodArgs.get(actionClass);
		if(classMethodArgs!=null){
			MethodParamBeanClass methodBeanClass=classMethodArgs.get(method);
			if(methodBeanClass!=null){
				return methodBeanClass.getMethod();
			}
		}
		throw new SmileRunException("获取action方法失败"+actionClass+" method:"+method+"不存在的方法");
	}
}
