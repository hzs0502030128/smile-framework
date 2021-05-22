package org.smile.beans.converter.type;

import java.util.Locale;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;
import org.smile.util.LocaleUtils;

/**
 * Converts given object to Java <code>Locale</code>.
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li>finally, string representation of the object is used for getting the locale</li>
 * </ul>
 */
public class LocaleConverter extends AbstractTypeConverter<Locale> {

	@Override
	public Class<Locale> getType() {
		return Locale.class;
	}

	@Override
	public Locale convert(Generic generic, Object value) throws ConvertException {
		if (value == null) {
			return null;
		}
		Object v=getFirst(value);
		if (v instanceof Locale) {
			return (Locale) v;
		}
		return LocaleUtils.getLocale(value.toString());
	}

}
