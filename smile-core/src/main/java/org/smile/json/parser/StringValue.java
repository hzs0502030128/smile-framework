package org.smile.json.parser;

import org.smile.commons.Strings;

public class StringValue {
	/***
	 * 有引号的字符串
	 * 会去掉引号转为字符串
	 * @param obj
	 * @return
	 */
	public static String valueOf(String obj) {
		return obj.substring(1, obj.length() - 1);
	}

	/**
	 * 没有引号的字符串转换
	 * 字符串为null里转换为null对象
	 * @param obj
	 * @return
	 */
	public static String noQuotedValueOf(String obj) {
		if (Strings.NULL.equals(obj)) {
			return null;
		} else {
			return obj;
		}
	}
}
