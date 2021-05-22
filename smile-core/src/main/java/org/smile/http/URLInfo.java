package org.smile.http;

import java.util.LinkedList;
import java.util.List;

import org.smile.commons.StringParam;
import org.smile.util.StringUtils;
/***
 * url地址解析使用
 * @author 胡真山
 *
 */
public class URLInfo {
	/**完整的url地址*/
	private String url;
	/**除去参数的url ? 之前的部分*/
	private String uri;
	/**uri 最后一个/之前的部分*/
	private String path;
	/**uri 最后一个/之后的部分*/
	private String name;
	/**参数拼接部他*/
	private String paramString;
	/**解析之后的参数键值列表*/
	private List<StringParam> params;
	/***
	 * 从字符串构造
	 * @param url
	 */
	public URLInfo(String url){
		this.url=url;
	}
	
	/***
	 * 解析url
	 */
	public void parse(){
		int index=url.indexOf('?');
		if(index>=0){
			this.uri=url.substring(0,index);
			this.paramString=url.substring(index+1);
		}else{
			this.uri=url;
		}
		index=uri.lastIndexOf('/');
		if(index>=0){
			path=uri.substring(0, index+1);
			name=uri.substring(index+1);
		}else{
			name=uri;
		}
		if(StringUtils.notEmpty(paramString)){
			params=new LinkedList<StringParam>();
			String[] str=StringUtils.split(paramString,"&");
			for(String s:str){
				if(StringUtils.notEmpty(s)){
					String[] p=StringUtils.split(s, "=");
					if(p.length==2){
						params.add(new StringParam(p[0], p[1]));
					}
				}
			}
		}
	}


	public String getUrl() {
		return url;
	}


	public String getUri() {
		return uri;
	}


	public String getPath() {
		return path;
	}


	public String getName() {
		return name;
	}


	public String getParamString() {
		return paramString;
	}


	public List<StringParam> getParams() {
		return params;
	}
	
}
