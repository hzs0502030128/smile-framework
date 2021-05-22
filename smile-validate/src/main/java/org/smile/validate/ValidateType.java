package org.smile.validate;

import java.util.Locale;

import org.smile.beans.BeanProperties;
import org.smile.validate.handler.DateTimeValidater;
import org.smile.validate.handler.DateValidater;
import org.smile.validate.handler.EmailValidater;
import org.smile.validate.handler.EqualToValidater;
import org.smile.validate.handler.LengthRangeValidater;
import org.smile.validate.handler.MaxLengthValidater;
import org.smile.validate.handler.MinLengthValidater;
import org.smile.validate.handler.NotEqualValidater;
import org.smile.validate.handler.NumberValidater;
import org.smile.validate.handler.ExpressionValidater;
import org.smile.validate.handler.RangeValidater;
import org.smile.validate.handler.RegExpValidater;
import org.smile.validate.handler.RequiredValidater;
import org.smile.validate.handler.TelephoneValidater;
import org.smile.validate.handler.Validater;

/**
 * 数据校验类型
 * 
 * @author 胡真山
 * @Date 2016年2月3日
 */
public enum ValidateType {
	/**
	 * 非空判断
	 */
	required(new RequiredValidater()),
	/**
	 * 数字区间
	 */
	range(new RangeValidater()),
	/**
	 * 长度区间
	 */
	lengthRange(new LengthRangeValidater()),
	/**
	 * 最大长度限制
	 */
	maxLength(new MaxLengthValidater()),
	/**
	 * 最小长度限制
	 */
	minLength(new MinLengthValidater()),
	/**
	 * 一样
	 */
	equalTo(new EqualToValidater()),
	/**
	 * 不一样
	 */
	notEqual(new NotEqualValidater()),
	/**
	 * 数字类型
	 */
	number(new NumberValidater()),
	/**
	 * 日期格式
	 */
	date(new DateValidater()),
	/**
	 * 日期日时格式
	 */
	datetime(new DateTimeValidater()),
	/**
	 * 时间类型
	 */
	time(new DateTimeValidater()),
	/** 电话号码 */
	telephone(new TelephoneValidater()),
	/** 电子邮箱地址 */
	email(new EmailValidater()),
	/** 正则表达式匹配 */
	regexp(new RegExpValidater()),
	
	/**smile表达式*/
	expression(new ExpressionValidater());

	private Validater validater;

	private ValidateType(Validater validater) {
		this.validater = validater;
	}

	public boolean validate(Rule f, ValidateSupport action, BeanProperties properties) throws Exception {
		return validater.validate(f, action, properties);
	}
	
	public Validater getValidater(){
		return validater;
	}
	/**
	 * 国际化提示文本
	 * @param locale
	 * @return
	 */
	public String getText(Locale locale) {
		return ValidateConstants.message.getString(locale, ValidateConstants.validateTextKey + toString());
	}
}
