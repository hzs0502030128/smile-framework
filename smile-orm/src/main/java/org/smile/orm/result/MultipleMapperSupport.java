package org.smile.orm.result;
/**
 * 多对象映射
 * @author 胡真山
 */
public interface MultipleMapperSupport {
	/**
	 * 获取映射类
	 * @param shortCut 数据库
	 * @return
	 */
	public Class getMapperClass(String shortCut);
}
