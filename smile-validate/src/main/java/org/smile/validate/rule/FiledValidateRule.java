package org.smile.validate.rule;

import org.smile.validate.CustomValidator;
import org.smile.validate.Field;
import org.smile.validate.Rule;
import org.smile.validate.ValidateType;

public class FiledValidateRule implements Rule{
	/**配置验证规则的注解*/
	protected Field f;
	
	public FiledValidateRule(Field f){
		this.f=f;
	}
	
	@Override
	public String getFieldName() {
		return f.name();
	}

	@Override
	public ValidateType getType() {
		return f.type();
	}

	@Override
	public Comparable[] getRange() {
		String[] r=f.range();
		if(f.type()==ValidateType.lengthRange){
			Integer[] range=new Integer[0];
			range[0]=Integer.parseInt(r[0]);
			range[1]=Integer.parseInt(r[1]);
			return range;
		}
		return f.range();
	}

	@Override
	public Object getValue() {
		return f.value();
	}

	@Override
	public String getRegexp() {
		return f.regexp();
	}

	@Override
	public String getText() {
		return f.text();
	}

	@Override
	public String getKey() {
		return f.key();
	}

	@Override
	public Class<? extends CustomValidator> getCustom() {
		return f.custom();
	}

	@Override
	public boolean ifnull() {
		return f.ifnull();
	}

}
