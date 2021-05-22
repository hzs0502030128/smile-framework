package org.smile.http.client;

import java.io.InputStream;

public class StreamValue extends KeyValue{
	
	protected InputStream inputStream;
	
	public StreamValue(String name,String value,InputStream inputStream){
		super(name, value);
		this.inputStream=inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public InputStream inputStream() {
		return inputStream;
	}

	@Override
	public boolean isStream() {
		return true;
	}
	
}
