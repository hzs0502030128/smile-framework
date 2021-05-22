package org.smile.beans;

import java.beans.PropertyDescriptor;

import org.smile.beans.converter.BeanException;

/**
 * 以是javabean方法写入数据和获取数据的属性
 * @author 胡真山
 * @Date 2016年1月8日
 */
public class BeanPropertyColumn implements BeanColumn<PropertyDescriptor> {

	/**属性索引*/
	private int index;
	/**属性定义*/
	private PropertyDescriptor pd;

	public BeanPropertyColumn(int index, PropertyDescriptor pd) {
		this.index = index;
		this.pd = pd;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public String getName() {
		return pd.getName();
	}

	@Override
	public Class getPropertyType() {
		return pd.getPropertyType();
	}

	@Override
	public PropertyDescriptor getProperty() {
		return pd;
	}

	@Override
	public void writeValue(Object target, Object value) throws BeanException {
		BeanUtils.setObjectProperty(target, pd, value);
	}

	@Override
	public Object getValue(Object target) throws BeanException {
		return BeanUtils.getObjectProperty(target, pd);
	}

}
