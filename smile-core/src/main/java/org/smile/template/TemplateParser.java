package org.smile.template;

/**
 * 模板解析接口
 * @author 胡真山
 *
 */
public interface TemplateParser {
	/**
	 * 替换符起始标记
	 */
	public static final String DEFAULT_MACRO_START = "${";
	/**
	 * 替换符结束标记
	 */
	public static final String DEFAULT_MACRO_END = "}";

	/**
	 * Parses string template and replaces macros with resolved values.
	 */
	public abstract String parse(String template, MacroResolver macroResolver);
	
	/**
	 * Defines macro start string.
	 */
	public void setMacroStart(String macroStart);
	
	/**
	 * Defines macro end string.
	 */
	public void setMacroEnd(String macroEnd);
	/**
	 * Specifies replacement for missing keys. If <code>null</code>
	 * exception will be thrown.
	 */
	public void setMissingKeyReplacement(String missingKeyReplacement);
	
	/**
	 * Specifies if missing keys should be resolved at all,
	 * <code>true</code> by default.
	 * If <code>false</code> missing keys will be left as it were, i.e.
	 * they will not be replaced.
	 */
	public void setReplaceMissingKey(boolean replaceMissingKey);
	
	/**
	 * Macro value resolver.
	 */
	public interface MacroResolver {
		/**
		 * Resolves macro value for macro name founded in string template.
		 * <code>null</code> values will be replaced with empty strings.
		 */
		String resolve(String macroName);

	}

	public interface Fragment{
		/**执行表达式*/
		String invokeExpress(MacroResolver macroResolver,String express);
		/**
		 * 此片断是否存在表达式
		 * @return
		 */
		boolean hasExpress();
	}

	/**
	 * 解析片断
	 * @param template
	 * @return
	 */
	public Fragment fragment(String template);

}