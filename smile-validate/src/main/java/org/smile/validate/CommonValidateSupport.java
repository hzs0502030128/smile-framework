package org.smile.validate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.smile.resource.MessageResource;

public class CommonValidateSupport implements ValidateSupport {

	/**用于对提示信息国际化*/
	protected Locale locale;
	/**保存验证错误信息*/
	protected Map<String, String> errors = new HashMap<String, String>();
	/**要验证的对象*/
	protected Object target;

	public CommonValidateSupport(Locale local) {
		this.locale = local;
	}
	
	public CommonValidateSupport(){
		this.locale=MessageResource.getRootLocale();
	}

	@Override
	public Locale locale() {
		return locale;
	}

	@Override
	public void addValidateError(String name, String msg) {
		errors.put(name, msg);
	}

	public CommonValidateSupport setTarget(Object target) {
		this.target = target;
		return this;
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}

	@Override
	public Object getTarget() {
		return target;
	}

}
