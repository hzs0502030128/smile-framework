package org.smile.http.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.collection.CollectionUtils;
import org.smile.commons.Strings;
import org.smile.http.HttpMethod;
import org.smile.json.JSONAware;
import org.smile.json.JSONValue;
import org.smile.json.parser.JSONParseException;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;

public abstract class AbstractHttpResponse<C> extends AbstractHttpBase<HttpResponse<C>> implements HttpResponse<C>{
	/**多个cookie之间的分隔*/
	protected static RegExp split=new RegExp(";");
	
	protected static final String RESPONSE_COOKIE_NAME="Set-Cookie";
	/**响应的状态码*/
	protected int status;
	
	/**
	 * set up url, method, header, cookies
	 * @param conn
	 * @param previousResponse
	 * @throws IOException
	 */
    public void setupFromConnection(HttpURLConnection conn, HttpResponse<?> previousResponse) throws IOException {
        method = HttpMethod.valueOf(conn.getRequestMethod());
        url = conn.getURL();
        statusCode = conn.getResponseCode();
        statusMessage = conn.getResponseMessage();
        contentType = conn.getContentType();

        Map<String, List<String>> resHeaders = createHeaderMap(conn);
        
        processResponseHeaders(resHeaders);

        // if from a redirect, map previous response cookies into this response
        if (previousResponse != null) {
        	Set<Map.Entry<String, String>> cookies=previousResponse.cookies().entrySet();
            for (Map.Entry<String, String> prevCookie : cookies) {
                if (!hasCookie(prevCookie.getKey())){
                    cookie(prevCookie.getKey(), prevCookie.getValue());
                }
            }
        }
    }
    
    protected Map<String, List<String>> createHeaderMap(HttpURLConnection conn) {
		Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
		int i = 0;
		while (true) {
			final String key = conn.getHeaderFieldKey(i);
			final String val = conn.getHeaderField(i);
			if (key == null && val == null) {
				break;
			}
			i++;
			if (key == null || val == null) {
				// skip http1.1 line
				continue; 
			}
			if (headers.containsKey(key)){
				headers.get(key).add(val);
			}else {
				List<String> vals = new ArrayList<String>();
				vals.add(val);
				headers.put(key, vals);
			}
		}
		return headers;
	}

	/***
	 * 从服务器返回的header信息解析
	 * @param respHeaders
	 */
	protected void processResponseHeaders(Map<String, List<String>> respHeaders) {
		for (Map.Entry<String, List<String>> entry : respHeaders.entrySet()) {
			String name = entry.getKey();
			if (name == null) continue; // http/1.1 line

			List<String> values = entry.getValue();
			if (name.equalsIgnoreCase(RESPONSE_COOKIE_NAME)) {//设置cookie
				for (String value : values) {
					if (value == null) continue;
					List<String> ss=split.splitToList(value);
					if(CollectionUtils.notEmpty(ss)){
						for(String c:ss){
							String[] kv=StringUtils.splitc(c,'=');
							if(kv.length>1){
								cookie(kv[0], kv[1]);
							}else{
								cookie(kv[0],Strings.BLANK);
							}
						}
					}
				}
			} else {
				if (values.size() == 1){
					header(name, values.get(0));
				}else if (values.size() > 1) {
					StringBuilder accum = new StringBuilder();
					for (int i = 0; i < values.size(); i++) {
						final String val = values.get(i);
						if (i != 0) accum.append(", ");
						accum.append(val);
					}
					header(name, accum.toString());
				}
			}
		}
	}

	@Override
	public <T extends JSONAware> T readJSON() throws JSONParseException {
		try {
			return (T) JSONValue.parse(readBody());
		} catch (UnsupportedEncodingException e) {
			throw new JSONParseException(e);
		}
	}

	@Override
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
