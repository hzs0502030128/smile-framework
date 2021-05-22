package org.smile.strate.dispatch;

import org.smile.commons.Strings;
import org.smile.strate.ActionConstants;
import org.smile.strate.action.ActionElement;
/**
 * 扩展名方式URL解析
 * @author 胡真山
 *
 */
public class ExtensionActionURIParser implements ActionURIParser{

	@Override
	public boolean isActionURI(String contextPath,String url) {
		return url.endsWith(getActionExtension());
	}
	/**
	 * action路径的扩展名
	 * @return
	 */
	public String getActionExtension() {
		return Strings.DOT+ActionConstants.extension;
	}

	@Override
	public ActionURLInfo parseURI(String contextPath,String uri) {
		// servlet路径
		String actionURI = uri.substring(uri.indexOf(contextPath) + contextPath.length(), uri.lastIndexOf("."));
		int index = actionURI.lastIndexOf(URL_PATH_SEPARATOR);
		String namespace = actionURI.substring(0, index + 1);
		String actionName = actionURI.substring(index + 1);
		return new ActionURLInfo(namespace, actionName);
	}

	@Override
	public String createURI(ActionElement actionElement) {
		return actionElement.getPackageElement().getNamespace()+actionElement.getName()+getActionExtension();
	}

}
