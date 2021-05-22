package org.smile.db.sql.parameter;

import org.smile.beans.BeanProperties;
import org.smile.beans.PropertyKeyType;
import org.smile.beans.converter.BeanException;
/**
 * bean 类型的参数设置
 * @author 胡真山
 *
 */
public  class BeanParameterFiller extends MappingParameterFiller{
	
	protected static BeanProperties beanProperties=new BeanProperties(PropertyKeyType.nocase);
	
	public BeanParameterFiller(ParameterMapping pmList){
		super(pmList);
	}

	@Override
	protected Object getParamterValueForm(String name, Object value) throws BeanException {
		return beanProperties.getExpFieldValue(value,name);
	}
}
