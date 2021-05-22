package org.smile.http.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.smile.commons.SmileRunException;
import org.smile.http.HttpMethod;

public class HttpPost extends AbstractHttpRequest<HttpPost>{
	
	public HttpPost(String url){
		try {
			this.method=HttpMethod.POST;
			url(new URL(url));
		} catch (MalformedURLException e) {
			throw new SmileRunException(e);
		}
	}
	
}
