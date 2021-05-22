package org.smile.http.client;

import java.io.IOException;

public interface HttpClient {
	/**执行一个http方法
	 * @throws IOException */
	public <C> HttpResponse<C> execute(HttpRequest<?> req) throws IOException;
}
