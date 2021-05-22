package org.smile.http.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.smile.commons.SmileRunException;
import org.smile.http.HttpMethod;


public class HttpGet extends AbstractHttpRequest<HttpGet>{
	
	public HttpGet(String url){
		try {
			this.method=HttpMethod.GET;
			url(new URL(url));
		} catch (MalformedURLException e) {
			throw new SmileRunException(e);
		}
	}
}
