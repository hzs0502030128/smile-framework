package org.smile.json;

import org.smile.util.StringUtils;

public class JSONFormatter {

	private static String SPACE = "	";

	/**
	 * 格式化json字符串
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static String formatJSON(String jsonStr) {
		if (StringUtils.isEmpty(jsonStr)) {
			return jsonStr;
		}
		StringBuilder sb = new StringBuilder((int) (jsonStr.length() * 1.5));
		char last = '\0';
		char current = '\0';
		int indent = 0;
		for (int i = 0; i < jsonStr.length(); i++) {
			last = current;
			current = jsonStr.charAt(i);
			// 遇到{ [换行，且下一行缩进
			switch (current) {
			case '{':
			case '[':
				sb.append(current);
				sb.append('\n');
				indent++;
				indent(sb, indent);
				break;
			// 遇到} ]换行，当前行缩进
			case '}':
			case ']':
				sb.append('\n');
				indent--;
				indent(sb, indent);
				sb.append(current);
				break;
			// 遇到,换行
			case ',':

				sb.append(current);

				if (last != '\\') {
					if (!isInString(jsonStr, i)) {
						sb.append('\n');
						indent(sb, indent);
					}
				}

				break;
			default:
				sb.append(current);
			}
		}

		return sb.toString();
	}

	/**
	 * 添加space
	 * 
	 * @param sb
	 * @param indent
	 */
	private static void indent(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append(SPACE);
		}
	}

	/**
	 * 查看当前字符是否是在字符串中
	 * 
	 * @param str
	 *            要检查的json字符串
	 * @param index
	 * @return
	 */
	private static boolean isInString(String str, int index) {
		// 查找前面
		char current;
		left: for (int i = index - 1; i > 0; i--) {
			current = str.charAt(i);
			switch (current) {
			case ':':
				return false;
			case '[':
				return false;
			case '{':
				return false;
			case '\"':
			case '\'':
				current = str.charAt(i - 1);
				if (current == ':' || current == '[' || current == ',' || current == '{') {
					break left;
				} else {
					return false;
				}
			}
		}
		int len = str.length();
		// 检查右边
		right: for (int i = index + 1; i < len; i++) {
			current = str.charAt(i);
			switch (current) {
			case ':':
				return false;
			case ']':
				return false;
			case '}':
				return false;
			case '\"':
			case '\'':
				current = str.charAt(i + 1);
				if (current == ']' || current == ',' || current == ':' || current == '}') {
					break right;
				} else {
					return false;
				}
			}
		}
		return true;
	}
}
