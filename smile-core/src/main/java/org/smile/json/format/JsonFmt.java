package org.smile.json.format;
/**
 * 转换成json时对数据进行格式化
 * @author 胡真山
 *
 */
public @interface JsonFmt {
	/**要格式化的格式*/
	public String pattern();
}
