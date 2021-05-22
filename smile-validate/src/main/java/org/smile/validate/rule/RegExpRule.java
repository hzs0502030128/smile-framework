package org.smile.validate.rule;

import org.smile.validate.ValidateType;
/**
 * 正则表达式验证
 * @author 胡真山
 *
 */
public class RegExpRule extends DefaultRule{
	
	public RegExpRule(String regexp){
		this.type=ValidateType.regexp;
		this.regexp=regexp;
	}

	@Override
	public Object getValue() {
		if(value==null){
			return regexp;
		}
		return value;
	}
	
}
