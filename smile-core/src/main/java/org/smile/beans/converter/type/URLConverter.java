package org.smile.beans.converter.type;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.smile.beans.converter.AbstractTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.reflect.Generic;

public class URLConverter extends AbstractTypeConverter<URL> {
	@Override
	public URL convert(Generic generic, Object value) throws ConvertException {
		value = getFirst(value);
		if (value == null) {
			return null;
		}

		if (value instanceof URL) {
			return (URL) value;
		}

		if (value instanceof File) {
			File file = (File) value;
			try {
				return file.toURI().toURL();
			} catch (MalformedURLException muex) {
				throw new ConvertException(muex);
			}
		}

		if (value instanceof URI) {
			URI uri = (URI) value;
			try {
				return uri.toURL();
			} catch (MalformedURLException muex) {
				throw new ConvertException(muex);
			}
		}

		try {
			return new URL(value.toString());
		} catch (MalformedURLException muex) {
			throw new ConvertException(muex);
		}
	}

	@Override
	public Class<URL> getType() {
		return URL.class;
	}
}
