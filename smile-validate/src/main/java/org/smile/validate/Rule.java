package org.smile.validate;

public interface Rule {
	/**要验证的字段名*/
	public String getFieldName();
	/**验证类型*/
	public ValidateType getType();
	/**数字区间,在数据大小区间 字符串长度是用到 等 数字类型的参数*/
	public Comparable[] getRange();
	/**字符串的值   在eqaul 和 notequal maxLength minLength 等字符串判断是用到*/
	public Object getValue();
	/**正则表达式匹配时用到*/
	public String getRegexp();
	/**不通过时字段名称提示语*/
	public String getText();
	/**不通过时字段名称提示语国际化字符名称*/
	public String getKey();
	/**自定义验证实现类*/
	public Class<? extends CustomValidator> getCustom();
	/**当值为空时返回*/
	public boolean ifnull();
}
