package org.smile.strate.dispatch;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.smile.commons.Chars;
import org.smile.commons.SmileRunException;
import org.smile.strate.ActionConstants;
import org.smile.strate.Strate;
import org.smile.strate.action.ActionElement;
import org.smile.util.SimpleTokenizer;
import org.smile.util.StringTokenizer;
import org.smile.util.StringUtils;
/**
 * 
 * @author 胡真山
 *
 */
public class StartFlagActionURIParser implements ActionURIParser{
	/**
	 * 用于标识是action的url起始
	 */
	private String actionUrlPrefix=ActionConstants.extension;
	/**
	 * 	命名空间分隔次数
	 *	actionUrlPrefix/mode/actionname
	 *	那么分隔为1
	 *	先解析前缀    -> 解析 namepace -> 解析actionname
	 * 
	 */
	private int namespaceSplitCount=-1;
	
	@Override
	public boolean isActionURI(String contextPath,String url) {
		if(namespaceSplitCount==-1) {
			if(url.indexOf(Chars.DOT)>0){
				return false;
			}
			int index=StringUtils.indexOf(url,URL_PATH_SEPARATOR,contextPath.length());
			if(index<0){
				return actionUrlPrefix.equals(url.substring(contextPath.length()));
			}else{
				return url.indexOf(getActionFlag(), contextPath.length())>=0;
			}
		}else {
			if(url.indexOf(getActionFlag(), contextPath.length())==-1) {
				return false;
			}
			String actionURI=url.substring(contextPath.length()+actionUrlPrefix.length()+2);
			String[] split=StringUtils.splitc(actionURI, URL_PATH_SEPARATOR);
			if(split.length<namespaceSplitCount+1) {
				return false;
			}
			if(split[namespaceSplitCount].indexOf(Chars.DOT)!=-1) {
				return false;
			}
			return true;
		}
		
	}

	/**
	 * 用来标识是action路径的字符串
	 * @return
	 */
	protected String getActionFlag(){
		return actionUrlPrefix.length()>0?URL_PATH_SEPARATOR+actionUrlPrefix+URL_PATH_SEPARATOR:String.valueOf(URL_PATH_SEPARATOR);
	}

	@Override
	public ActionURLInfo parseURI(String contextPath,String uri) {
		// servlet路径
		String actionURI = uri.substring(uri.indexOf(contextPath) + contextPath.length());
		if(namespaceSplitCount!=-1) {//有配置空间的分隔个数时,支持url中带参数
			StringTokenizer tokenizer=new SimpleTokenizer(actionURI.substring(getActionFlag().length()),"/");
			int i=0;
			StringBuilder namespace=new StringBuilder("/");
			while(i<namespaceSplitCount) {
				namespace.append(tokenizer.nextToken());
				namespace.append(tokenizer.separator());
				i++;
			}
			String actionName=tokenizer.nextToken();
			ActionURLInfo actionUrlInfo= new ActionURLInfo(namespace.toString(),actionName);
			//处理参数
			if(tokenizer.hasMoreTokens()) {
				List<String> args=new ArrayList<String>();
				while(tokenizer.hasMoreTokens()) {
					try {
						args.add(URLDecoder.decode(tokenizer.nextToken(),Strate.encoding));
					} catch (UnsupportedEncodingException e) {
						throw new SmileRunException(e);
					}
				}
				actionUrlInfo.setUriArgs(args.toArray(new String[args.size()]));
			}
			return actionUrlInfo;
		}else {
			int index = actionURI.lastIndexOf(URL_PATH_SEPARATOR);
			String namespace = actionURI.substring(getActionFlag().length()-1, index + 1);
			String actionName = actionURI.substring(index + 1);
			return new ActionURLInfo(namespace, actionName);
		}
	}


	@Override
	public String createURI(ActionElement actionElement) {
		return URL_PATH_SEPARATOR+actionUrlPrefix+actionElement.getPackageElement().getNamespace()+actionElement.getName();
	}

	public void setActionUrlPrefix(String actionUrlPrefix) {
		this.actionUrlPrefix = actionUrlPrefix;
	}

	public void setNamespaceSplitCount(int namespaceSplitCount) {
		this.namespaceSplitCount = namespaceSplitCount;
	}

}
