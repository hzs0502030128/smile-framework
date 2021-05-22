package org.smile.strate.form;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.smile.beans.BeanInfo;
import org.smile.beans.BeanProperties;
import org.smile.beans.MapBean;
import org.smile.beans.converter.BeanException;
import org.smile.beans.handler.MapBeanPropertyHandler;
import org.smile.collection.ArrayUtils;
import org.smile.http.RequestUtils;
import org.smile.reflect.MethodParamBeanClass;
import org.smile.strate.ActionConstants;
import org.smile.strate.StrateException;
import org.smile.strate.action.Action;
import org.smile.strate.action.ActionElement;
import org.smile.strate.action.ClassElement;
import org.smile.strate.dispatch.ActionURLInfo;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.util.StringUtils;

/**
 * request 数据赋值
 * @author 胡真山
 * @Date 2016年1月19日
 */
public class ActionParameterFiller {
	/**没有属性时不抛出异常*/
	public static MapBeanPropertyHandler handler = new MapBeanPropertyHandler(false);
	
	protected static boolean isEmptyField(Object value){
		if(value==null){
			return true;
		}
		if(ArrayUtils.isEmpty(value)){
			return true;
		}
		return false;
	}
	
	/**
	 * 设置request中的数据到action属性中
	 * @param request
	 * @param target action目标
	 * @return
	 * @throws BeanException
	 */
	public static ActionFormResult fillRequestToAction(DispatchContext context) throws BeanException {
		ActionFormResult result = new ActionFormResult();
		Action target=context.getAction();
		ActionElement actionElement=context.getActionElement();
		HttpServletRequest request=context.getRequest();
		ActionURLInfo actionUrlInfo=context.getActionUrlInfo();
		if(target.needRequestToAction()){
			BeanProperties beanProperties = new BeanProperties(false);
			beanProperties.setConverter(StrateConverter.getInstance(target.getClass()));
			Enumeration<String> enuma = request.getParameterNames();
			Map paramters=request.getParameterMap();
			while (enuma.hasMoreElements()) {
				String key = enuma.nextElement();
				// 动态切换方法
				if (ActionConstants.allowDynamicMethod) {
					if (key.startsWith(ActionConstants.dynamicMethodFlag)) {
						String dynamicAction = key.substring(ActionConstants.dynamicMethodFlag.length());
						result.setDynamicAction(dynamicAction);
					}
				}
				Object value = paramters.get(key);
				if (isEmptyField(value)) {
					continue;
				}
				beanProperties.setExpFieldValue(target, RequestUtils.convertArrayKeyToProperty(key), value);
			}
			//处理url中的参数 /{name}/{age}
			if(ArrayUtils.notEmpty(actionElement.getUrlParams())&&actionUrlInfo!=null) {
				String[] argNames=actionElement.getUrlParams();
				String[] argValues=actionUrlInfo.getUriArgs();
				if(ArrayUtils.notEmpty(argValues)) {
					for(int i=0;i<argNames.length;i++) {
						if(i>argValues.length) {
							break ;
						}
						beanProperties.setExpFieldValue(target, argNames[i], argValues[i]);
					}
				}
			}
		}
		return result;
	}

	public static ActionFormResult fillRequestToAction(DispatchContext context, ActionParamBeanClass beanClass) throws BeanException, StrateException {
		Action target=context.getAction();
		ActionElement actionElement=context.getActionElement();
		HttpServletRequest request=context.getRequest();
		ActionURLInfo actionUrlInfo=context.getActionUrlInfo();
		BeanProperties beanProperties = new BeanProperties(false);
		
		beanProperties.setConverter(StrateConverter.getInstance(target.getClass()));
		
		MapBean<ActionParamBeanClass> paramBean=beanClass.newInstance();
		//读取请求body数据
		if(beanClass.hasRequestBody()) {
			beanClass.doRequestBody(target, paramBean);
		}
		beanClass.setRequestAndResponse(paramBean, request, context.getResponse());
		//参数数据
		Enumeration<String> enuma = request.getParameterNames();
		//使用新建的 用于对转换器的重写
		paramBean.setBeanProperties(beanProperties);
		ActionFormResult result = new ActionFormResult();
		Map paramters=request.getParameterMap();
		while (enuma.hasMoreElements()) {
			String key = enuma.nextElement();
			// 动态切换方法
			if (ActionConstants.allowDynamicMethod) {
				if (key.startsWith(ActionConstants.dynamicMethodFlag)) {
					String dynamicAction = key.substring(ActionConstants.dynamicMethodFlag.length());
					result.setDynamicAction(dynamicAction);
				}
			}
			Object value = paramters.get(key);
			if (isEmptyField(value)) {
				continue;
			}
			key = RequestUtils.convertArrayKeyToProperty(key);
			if(target.needRequestToAction()){//需要设置属性到action对象中
				beanProperties.setExpFieldValue(target, key, value);
			}
			//设置对方法封装的mapbean中
			handler.setExpFieldValue(paramBean, key, value);
		}
		//处理url中的参数 /{name}/{age}
		if(ArrayUtils.notEmpty(actionElement.getUrlParams())&&actionUrlInfo!=null) {
			String[] argNames=actionElement.getUrlParams();
			String[] argValues=actionUrlInfo.getUriArgs();
			if(ArrayUtils.notEmpty(argValues)) {
				for(int i=0;i<argNames.length;i++) {
					if(i>argValues.length) {
						break ;
					}
					handler.setExpFieldValue(paramBean, argNames[i], argValues[i]);
				}
			}
		}
		result.setMethodParamBean(paramBean);
		return result;
	}

	/**
	 * 把action的值设置到request中
	 * @param request
	 * @param target
	 * @throws BeanException
	 */
	public static void fillActionToRequest(HttpServletRequest request, ActionElement actionElement, Action target) throws BeanException {
		if (target == null) {
			return;
		}
		BeanInfo beaninfo = BeanInfo.getInstance(target.getClass());
		PropertyDescriptor[] pds = beaninfo.getPropertyDescriptors();
		ClassElement clazzEle=actionElement.getClassElement();
		for (PropertyDescriptor pd : pds) {
			Method reader = pd.getReadMethod();
			if (reader!= null&&!clazzEle.existsActionByMethodName(reader.getName())) {
				try {
					Object value = reader.invoke(target);
					if (value != null) {
						request.setAttribute(pd.getName(), value);
					}
				} catch (Exception e) {
					throw new BeanException(target.getClass() + "action getter invoke error " + pd, e);
				}
			}
		}
	}
}
