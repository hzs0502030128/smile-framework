package org.smile.report.excel.target;

import org.smile.beans.BeanProperties;
import org.smile.beans.PropertyKeyType;
import org.smile.commons.SmileRunException;
import org.smile.reflect.ClassTypeUtils;

public class BeanImportTarget extends ImportTarget{
	
	private static BeanProperties beanProperties=new BeanProperties(PropertyKeyType.nocase,false);
	private Class targetType;
	public BeanImportTarget(Class targetType){
		this.targetType=targetType;
	}

	@Override
	public Object newTargetInstanse() {
		try {
			return ClassTypeUtils.newInstance(this.targetType);
		} catch (Exception e) {
			throw new SmileRunException("构建封装对象失败"+this.targetType,e);
		}
	}

	@Override
	public BeanProperties getBeanProperties() {
		return beanProperties;
	}

}
