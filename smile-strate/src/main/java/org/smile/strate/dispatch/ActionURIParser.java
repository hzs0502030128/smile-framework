package org.smile.strate.dispatch;

import org.smile.strate.action.ActionElement;

public interface ActionURIParser {
	/**url路径中的分隔符*/
	public static char URL_PATH_SEPARATOR='/';
	/**
	 * 判断是否是action的地址
	 */
	public boolean isActionURI(String contextPath, String uri);
	/***
	 * 对action的url地址进行解析出action名称
	 * @param contextPath
	 * @param uri
	 * @return
	 */
	public ActionURLInfo parseURI(String contextPath, String uri);
	/***
	 * 由配置信息转成action地址url
	 * @param actionElement
	 * @return
	 */
	public String createURI(ActionElement actionElement);
}
