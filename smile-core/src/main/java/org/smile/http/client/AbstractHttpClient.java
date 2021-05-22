package org.smile.http.client;

import org.smile.util.RegExp;

public abstract class AbstractHttpClient implements HttpClient{
	/***/
	protected static final RegExp CONTENT_TYPE_CHAREST=new RegExp(" *; *charset *= *",false);
	/**
	 * 对特殊字符进行处理
	 * @param url
	 * @return
	 */
	protected String encodeUrl(String url){
		if(url==null){
			return null;
		}
		StringBuilder sb=new StringBuilder((int)(url.length()*1.2));
		for(int i=0;i<url.length();i++){
			char c=url.charAt(i);
			switch(c){
				case ' ':
					sb.append("%20");
					break;
				case '+':
					sb.append("%2b");
					break;
				case '=':
					sb.append("%3D");
					break;
				case '\'':
					sb.append("%27");
					break;
				case '/':
					sb.append("%2F");
					break;
				case '.':
					sb.append("%2E");
					break;
				case '<':
					sb.append("%3c");
					break;
				case '>':
					sb.append("%3e");
					break;
				case '#':
					sb.append("%23");
					break;
				case '%':
					sb.append("%25");
					break;
				case '&':
					sb.append("%26");
					break;
				case '{':
					sb.append("%7B");
					break;
				case '}':
					sb.append("%7D");
					break;
				case '@':
					sb.append("%40");
					break;
				default:sb.append(c);
			}
		}
		return sb.toString();
	}
}
