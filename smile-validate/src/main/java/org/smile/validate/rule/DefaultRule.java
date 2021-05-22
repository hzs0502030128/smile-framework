package org.smile.validate.rule;

import org.smile.validate.CustomValidator;
import org.smile.validate.Rule;
import org.smile.validate.ValidateType;

public class DefaultRule implements Rule{
	/**
	 * 要验证的字段名称
	 */
	protected String fieldName;
	/***
	 * 验证类型
	 */
	protected ValidateType type;
	/**
	 * 用于验证配置的值
	 */
	protected Object value;
	/**
	 * 用于验证的正则表达式
	 */
	protected String regexp;
	/**
	 * 错误提示信息的字段名称
	 */
	protected String text;
	/**
	 * 用于提示的字段名称对应的资源文件key
	 */
	protected String key;
	/**
	 * 自定义的验证类
	 */
	protected Class<? extends CustomValidator> custom;
	/**
	 * 用于区间验证
	 */
	protected Comparable<?>[] rang;
	
	protected boolean ifnull=false;
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public void setField(String name,String text){
		this.fieldName=name;
		this.text=text;
	}

	public void setType(ValidateType type) {
		this.type = type;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setCustom(Class<? extends CustomValidator> custom) {
		this.custom = custom;
	}

	public void setRang(Comparable<?>[] rang) {
		this.rang = rang;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public ValidateType getType() {
		return type;
	}

	@Override
	public Comparable<?>[] getRange() {
		return rang;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String getRegexp() {
		return regexp;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Class<? extends CustomValidator> getCustom() {
		return custom;
	}


	public void setIfnull(boolean ifnull) {
		this.ifnull = ifnull;
	}

	@Override
	public boolean ifnull() {
		return this.ifnull;
	}

}
