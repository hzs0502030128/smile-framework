package org.smile.http.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.smile.commons.SmileRunException;
import org.smile.http.HttpMethod;

public class DefaultHttpRequest extends AbstractHttpRequest<DefaultHttpRequest>{
	
	public  DefaultHttpRequest(HttpMethod method,String url){
		super();
		this.method=method;
		try {
			this.url=new URL(url);
		} catch (MalformedURLException e) {
			throw new SmileRunException(e);
		}
	}
}
