package org.smile.validate.handler;

import org.smile.beans.BeanProperties;
import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyHandlers;
import org.smile.beans.converter.BeanException;
import org.smile.util.StringUtils;
import org.smile.validate.IValidater;
import org.smile.validate.Rule;
import org.smile.validate.ValidateConstants;
import org.smile.validate.ValidateSupport;

public abstract class Validater implements IValidater{
	/**
	 * 获取字段的值
	 * @param f
	 * @param action
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public Object getValue(Rule f,ValidateSupport action,BeanProperties properties) throws BeanException{
		Object target=action.getTarget();
		PropertyHandler handler=PropertyHandlers.getHanlder(target.getClass(), properties);
		return handler.getExpFieldValue(target, f.getFieldName());
	}
	/**
	 * 不通过时的错误提示语
	 * @param f
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getErrorMsg(Rule f,ValidateSupport action){
		String text=f.getType().getText(action.locale());
		if(StringUtils.isEmpty(text)){
			//如果没有国际化时获取配置资源
			text=defaultTxt();
		}
		return text;
	}
	/**
	 * 默认的验证提示语
	 * @return
	 */
	public abstract String defaultTxt();
	/**
	 * 获取验证字段的名称
	 * @param f 验证的字段
	 * @param action
	 * @return
	 */
	public String getFieldNameTxt(Rule f,ValidateSupport action){
		String text=null;
		String key=f.getKey();
		//如果有配置验证字段的名称key时，使用国际方式取key的对应国际化资源value
		if(StringUtils.notEmpty(key)){
			text=ValidateConstants.message.getString(action.locale(), key);
		}else{
			text=f.getText();
		}
		return StringUtils.isEmpty(text)?f.getFieldName():text;
	}
	/**
	 * 不通过时获取错误信息
	 * @param action
	 * @param f
	 * @param args
	 * @throws Exception
	 */
	public void error(ValidateSupport action,Rule f,Object... args){
		String text=getErrorMsg(f, action);
		action.addValidateError(f.getFieldName(),StringUtils.replaceFlag(text, getFieldNameTxt(f, action),args));
	}
}
