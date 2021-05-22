package org.smile.validate;

import java.lang.reflect.Method;

import org.smile.commons.SmileRunException;
import org.smile.validate.rule.FiledValidateRule;

public class ValidateElementUtils {
	/**
	 * 初始始化一个类的方法 的注解验证配置信息
	 * @param clazz 类
	 * @param method 要验证的方法
	 * @return
	 */
	public static ValidateElement checkAnnotationValidate(Method method){
		try {
			Validate validate = method.getAnnotation(Validate.class);
			if (validate != null) {
				Field[] fs = validate.fields();
				FiledValidateRule[] rules=new FiledValidateRule[fs.length];
				int index=0;
				for(Field f:fs){
					rules[index++]=new FiledValidateRule(f);
				}
				return new ValidateElement(rules);
			}
		} catch (Exception e) {
			throw new SmileRunException("初始化注解验证配置信息出错"+method.getDeclaringClass()+" method "+method, e);
		}
		return null;
	}
	/**
	 * 从注解验证初妈化一个验证元素
	 * @param fs
	 * @return
	 */
	public static ValidateElement newValidateElement(Field[] fs){
		FiledValidateRule[] rules=new FiledValidateRule[fs.length];
		int index=0;
		for(Field f:fs){
			rules[index++]=new FiledValidateRule(f);
		}
		return new ValidateElement(rules);
	}
}
