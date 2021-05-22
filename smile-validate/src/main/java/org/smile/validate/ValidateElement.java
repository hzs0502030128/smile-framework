package org.smile.validate;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.commons.SmileRunException;

public class ValidateElement {
	/**是否是单个验证不通过返回*/
	private boolean single=ValidateConstants.single;
	
	protected BeanProperties properties=BeanProperties.NORAL;
	/**验证规则*/
	private Rule[] rules;
	
	public ValidateElement(Rule[] rules){
		this.rules=rules;
	}
	
	public boolean validate(ValidateSupport action){
		//记录验证的结果
		boolean result=true;
		for(Rule f:rules){
			IValidater validater;
			if(f.getCustom()!=null&&f.getCustom()!=NULLValidater.class){
				//如果有自定义验证类，初始化实例
				try {
					validater=f.getCustom().newInstance().getValidater();
				} catch (Exception e) {
					throw new SmileRunException("自定义验证实例化出错",e);
				}
			}else{
				validater=f.getType().getValidater();
			} 
			try {
				if(!validater.validate(f, action, properties)){
					if(single){
						//单个验证时立即结束验证
						return false;
					}else{
						result=false;
					}
				}
			} catch (BeanException e) {
				throw new SmileRunException(e);
			}
		}
		return result;
	}

	/**是否单个验证*/
	public boolean isSingle() {
		return single;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}
	
}
