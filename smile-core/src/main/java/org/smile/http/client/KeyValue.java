package org.smile.http.client;

import java.io.InputStream;

import org.smile.commons.StringParam;

public class KeyValue extends StringParam implements RequestValue{

	public KeyValue(String name, String value) {
		super(name, value);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String value() {
		return value;
	}

	@Override
	public InputStream inputStream() {
		return null;
	}

	@Override
	public boolean isStream() {
		return false;
	}
	
}
