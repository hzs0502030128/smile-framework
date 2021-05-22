package org.smile.db.sql.parameter;


/**
 * 基本数据类型 (String Date Number)
 * 当为基本数据类型时
 * 直接返回对象
 * */
public  class BasicObjectParameterFiller extends MappingParameterFiller{
	
	public BasicObjectParameterFiller(ParameterMapping pmList){
		super(pmList);
	}

	/**
	 * 当为基本数据类型时
	 * 直接返回对象
	 */
	@Override
	protected Object getParamterValueForm(String name, Object value) {
		return value;
	}
}
