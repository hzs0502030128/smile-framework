package org.smile.orm.result;


/**
 * 返回类型为void
 * @author 胡真山
 * 2015年10月29日
 */
public class VoidResultType extends BaseResultType {

	@Override
	public Class getGenericType() {
		return null;
	}

	@Override
	public void onInit() {
		
	}

}
