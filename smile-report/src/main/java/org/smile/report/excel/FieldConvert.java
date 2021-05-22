package org.smile.report.excel;

/**
 * 
 * @author 胡真山
 * 用于字段转换内容
 */
public interface FieldConvert{
	/**
	 * 数据转换
	 * @param value excel中的值
	 * @return 转换
	 */
	public Object convert(Object value);
}
