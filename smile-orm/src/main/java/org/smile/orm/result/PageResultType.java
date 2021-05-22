package org.smile.orm.result;

import org.smile.db.PageModel;
/**
 * 分页返回类型
 * @author 胡真山
 *
 */
public class PageResultType extends BaseResultType{
	
	private Class genericType;
	
	public PageResultType(Class genericType){
		this.type=PageModel.class;
		this.genericType=genericType;
	}
	
	@Override
	public Class getGenericType() {
		return genericType;
	}
}
