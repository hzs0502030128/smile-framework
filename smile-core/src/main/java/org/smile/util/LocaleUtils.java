package org.smile.util;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import org.smile.collection.ThreadLocalMap;
import org.smile.commons.Strings;

/**
 * Locale utilities.
 */
public class LocaleUtils {

	/**
	 * Holds all per-Locale data.
	 */
	private static class LocaleData {
		LocaleData(Locale locale) {
			this.locale = locale;
		}
		final Locale locale;
		DateFormatSymbols dateFormatSymbols;
		NumberFormat numberFormat;
	}


	protected static Map<String, LocaleData> locales = new ThreadLocalMap<String, LocaleData>();

	/**
	 * Lookups for locale data and creates new if it doesn't exist.
	 */
	protected static LocaleData lookupLocaleData(String code) {
		LocaleData localeData = locales.get(code);
		if (localeData == null) {
			String[] data = decodeLocaleCode(code);
			localeData = new LocaleData(new Locale(data[0], data[1], data[2]));
			locales.put(code, localeData);
		}
		return localeData;
	}

	protected static LocaleData lookupLocaleData(Locale locale) {
		return lookupLocaleData(resolveLocaleCode(locale));
	}


	/**
	 * Returns Locale from cache.
	 */
	public static Locale getLocale(String language, String country, String variant) {
		LocaleData localeData = lookupLocaleData(resolveLocaleCode(language, country, variant));
		return localeData.locale;
	}

	/**
	 * Returns Locale from cache.
	 */
	public static Locale getLocale(String language, String country) {
		return getLocale(language, country, null);
	}

	/**
	 * Returns Locale from cache where Locale may be specified also using language code.
	 * Converts a locale string like "en", "en_US" or "en_US_win" to <b>new</b> Java locale object.
	 */
	public static Locale getLocale(String languageCode) {
		LocaleData localeData = lookupLocaleData(languageCode);
		return localeData.locale;
	}

	// ---------------------------------------------------------------- convert

	/**
	 * Transforms locale data to locale code. <code>null</code> values are allowed.
	 */
	public static String resolveLocaleCode(String lang, String country, String variant) {
		StringBuilder code = new StringBuilder(lang);
		if (StringUtils.isEmpty(country) == false) {
			code.append(Strings.UNDERSCORE).append(country);
			if (StringUtils.isEmpty(variant) == false) {
				code.append(Strings.UNDERSCORE).append(variant);
			}
		}
		return code.toString();
	}

	/**
	 * Resolves locale code from locale.
	 */
	public static String resolveLocaleCode(Locale locale) {
		return resolveLocaleCode(locale.getLanguage(), locale.getCountry(), locale.getVariant());
	}

	/**
	 * Decodes locale code in string array that can be used for <code>Locale</code> constructor.
	 */
	public static String[] decodeLocaleCode(String localeCode) {
		String result[] = new String[3];
		String[] data = StringUtils.split(localeCode, Strings.UNDERSCORE);
		result[0] = data[0];
		result[1] = result[2] = Strings.EMPTY;
		if (data.length >= 2) {
			result[1] = data[1];
			if (data.length >= 3) {
				result[2] = data[2];
			}
		}
		return result;
	}


	/**
	 * Returns cached <code>DateFormatSymbols</code> instance for specified locale.
	 */
	public static DateFormatSymbols getDateFormatSymbols(Locale locale) {
		LocaleData localeData = lookupLocaleData(locale);
		DateFormatSymbols dfs = localeData.dateFormatSymbols;

		if (dfs == null) {
			dfs = new DateFormatSymbols(locale);
			localeData.dateFormatSymbols = dfs;
		}

		return dfs;
	}
	/**
	 * Returns cached <code>NumberFormat</code> instance for specified locale.
	 */
	public static NumberFormat getNumberFormat(Locale locale) {
		LocaleData localeData = lookupLocaleData(locale);
		NumberFormat nf = localeData.numberFormat;
		if (nf == null) {
			nf = NumberFormat.getInstance(locale);
			localeData.numberFormat = nf;
		}
		return nf;
	}
}