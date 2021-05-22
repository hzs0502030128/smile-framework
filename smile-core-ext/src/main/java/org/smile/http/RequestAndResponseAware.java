package org.smile.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 需要对request response 知晓
 * @author 胡真山
 *
 */
public interface RequestAndResponseAware {
	/**设置 request response 的引用*/
	public void setRequestAndResponse(HttpServletRequest request, HttpServletResponse response, HttpMethod method);
	/**清除掉request response的引用 */
	public void clearRequestAndResponse();
}
