package org.smile.validate.handler;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.expression.Engine;
import org.smile.util.StringUtils;
import org.smile.validate.Rule;
import org.smile.validate.ValidateSupport;
import org.smile.validate.ValidateText;

public class ExpressionValidater extends Validater {
	@Override
	public boolean validate(Rule f, ValidateSupport action, BeanProperties properties) throws BeanException {
		String ognl=f.getValue().toString();
		Engine engine=Engine.getInstance();
		Boolean result=(Boolean) engine.evaluate(engine.createContext(action),ognl);
		if(!result){
			error(action, f,StringUtils.replaceAll(ognl,"\\$",""));
		}
		return result;
	}

	@Override
	public String defaultTxt() {
		return ValidateText.expression;
	}

}
