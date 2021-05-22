package org.smile.strate.i18n;

import java.util.Locale;

public interface I18nSupport {
	/**
	 * 获取国际化文字
	 * @param key
	 * @return
	 */
	public String getText(String key);
	/**
	 * 获取国际化文字
	 * @param local
	 * @param key
	 * @return
	 */
	public String getText(Locale local, String key);
}
