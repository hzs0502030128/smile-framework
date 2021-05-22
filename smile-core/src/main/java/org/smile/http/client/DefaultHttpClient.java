package org.smile.http.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.smile.commons.SmileRunException;
import org.smile.http.HttpConstans;
import org.smile.http.HttpMethod;
import org.smile.http.HttpStatusException;
import org.smile.io.IOUtils;
import org.smile.util.StringUtils;

public class DefaultHttpClient extends  AbstractHttpClient implements HttpConstans{
	
	private static final String LOCATION = "Location";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    private static final int HTTP_TEMP_REDIR = 307; // http/1.1 temporary redirect, not in Java's set.
	private static final String COOKIE="Cookie";
	private static SSLSocketFactory sslSocketFactory;

	@Override
	public <C> HttpResponse<C> execute(HttpRequest<?> req) throws IOException{
		return execute(req,null,null);
	}
	
	protected <C> HttpResponse<C> initNewReponse(HttpURLConnection conn,HttpResponse<?> previousResponse,AbstractHttpResponse<C> newResponse) throws IOException{
		 if(newResponse==null){
			 newResponse=  newInstanceResponse();
		 }
		 newResponse.setupFromConnection(conn, previousResponse);
		 newResponse.setStatus(conn.getResponseCode());
         return  (HttpResponse<C>)newResponse;
	}
	/**
	 *  默认实现response 
	 * 
	 * @param <C>
	 * @return
	 */
	protected <C> AbstractHttpResponse<C> newInstanceResponse(){
		return  (AbstractHttpResponse<C>) new DefaultHttpResponse();
	}
	/**
	 *	执行一个请求,指定response的实现对象
	 * @param <C>
	 * @param req
	 * @param newResponse 用于接收的response实例 
	 * @return
	 * @throws IOException
	 */
	public <C> HttpResponse<C> execute(HttpRequest<?> req,AbstractHttpResponse<C> newResponse) throws IOException{
		return execute(req,null, newResponse);
	}
	
	public <C> HttpResponse<C> execute(HttpRequest<?> req,HttpResponse<C> previousResponse,AbstractHttpResponse<C> newResponse) throws IOException{
        String protocol = req.url().getProtocol();
        if (!protocol.equals("http") && !protocol.equals("https")){
            throw new MalformedURLException("Only http & https protocols supported");
        }
        final boolean methodHasBody = req.method().hasBody();
        if (!methodHasBody&&req.hasRequestBody()){
            throw new SmileRunException("Cannot set a request body for HTTP method " + req.method());
        }
        // set up the request for execution
        String mimeBoundary = null;
        if (needSerialiseUrl(req)){
            serialiseRequestUrl(req);
        }else if(methodHasBody){
            mimeBoundary = setOutputContentType(req);
        }
        if(req.hasRequestBody()&&req.contentType()!=null){
        	List<String> contentTypes=CONTENT_TYPE_CHAREST.splitToList(req.contentType());
        	if(contentTypes.size()>1){
        		req.header(CONTENT_TYPE, contentTypes.get(0)+"; charset=" + req.postDataCharset());
        	}else{
        		req.header(CONTENT_TYPE,req.contentType()  + "; charset=" + req.postDataCharset());
        	}
        }
        
        HttpResponse<C> resp;
        HttpURLConnection conn = createConnection(req);
        try {
            conn.connect();
            if (conn.getDoOutput()){
                writePostDatas(req, conn.getOutputStream(), mimeBoundary);
            }
            int status = conn.getResponseCode();
            resp=initNewReponse(conn,previousResponse,newResponse);
            // redirect if there's a location header (from 3xx, or 201 etc)
            if (resp.hasHeader(LOCATION) && req.followRedirects()) {
                if (status != HTTP_TEMP_REDIR) {
                    req.method(HttpMethod.GET); // always redirect with a get. any data param from original req are dropped.
                    req.datas().clear();
                }

                String location = resp.header(LOCATION);
                if (location != null && location.startsWith("http:/") && location.charAt(6) != '/') // fix broken Location: http:/temp/AAG_New/en/index.php
                    location = location.substring(6);
                req.url(resolve(req.url(), location));
                Set<Map.Entry<String, String>>cookies=resp.cookies().entrySet();
                for (Map.Entry<String, String> cookie : cookies) { // add response cookies to request (for e.g. login posts)
                    req.cookie(cookie.getKey(), cookie.getValue());
                }
                return execute(req,resp,null);
            }
            if ((status < HttpConstans.STATUS_OK || status >= HttpConstans.STATUS_BAD) && !req.ignoreHttpErrors()){
                    throw new HttpStatusException("HTTP error fetching URL", status, req.url().toString());
            }
            // check that we can handle the returned content type; if not, abort before fetching it
            if(StringUtils.notEmpty(resp.contentType())){
            	parseReponseCharset(resp);
            }
            if (conn.getContentLength() != 0 && req.method() != HttpMethod.HEAD) { // -1 means unknown, chunked. sun throws an IO exception on 500 response with no content when trying to read body
                InputStream bodyStream = null;
                try {
                    bodyStream = conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream();
                    if (resp.hasHeaderWithValue(CONTENT_ENCODING, "gzip")){
                        bodyStream = new GZIPInputStream(bodyStream);
                    }
                    resp.recieveContent(bodyStream,req.maxBodySize());
                } finally {
                   IOUtils.close(bodyStream);
                }
            }
        } finally {
            conn.disconnect();
        }
        return resp;
	}
	/**
	 * 从返回的contenttype 中解析charset
	 * @param resp
	 */
	public <C> void parseReponseCharset(HttpResponse<?> resp){
		List<String> contentTypes=CONTENT_TYPE_CHAREST.splitToList(resp.contentType());
	   	 if(contentTypes.size()>1){
	   		 resp.contentType(contentTypes.get(0));
	   		 String charset=contentTypes.get(1);
	   		 if(Charset.isSupported(charset)){
	   			 resp.charset(charset);
	   		 }else{
	   			 String upper=charset.toLowerCase(Locale.ENGLISH);
	   			 if(Charset.isSupported(upper)){
	   				 resp.charset(upper);
	   			 }
	   		 }
	   	 }
	}
	
	private boolean needSerialiseUrl(HttpRequest<?> req){
		return req.datas().size() > 0 && (!req.method().hasBody() || req.hasRequestBody());
	}
	
	/**
	 * set up connection defaults, and details from request
	 * @param req
	 * @return
	 * @throws IOException
	 */
	protected HttpURLConnection createConnection(HttpRequest<?> req) throws IOException {
        HttpURLConnection conn = openConnection(req);
        if (conn instanceof HttpsURLConnection) {
            if(req.validateTLSCertificates()) {
            	
            }else{//无安全证书
                initUnSecureTSL();
                ((HttpsURLConnection)conn).setSSLSocketFactory(sslSocketFactory);
                ((HttpsURLConnection)conn).setHostnameVerifier(getInsecureVerifier());
            }
        }
        if (req.method().hasBody())
            conn.setDoOutput(true);
        if (req.cookies().size() > 0){
            conn.addRequestProperty(COOKIE, getRequestCookieString(req));
        }
        Set<Map.Entry<String, String>> headers=req.headers().entrySet();
        for (Map.Entry<String, String> header : headers) {
            conn.addRequestProperty(header.getKey(), header.getValue());
        }
        return conn;
    }
    
	protected HttpURLConnection openConnection(HttpRequest<?> request) throws IOException {
		HttpURLConnection connection;
		if(request.hasProxy()){
			connection= (HttpURLConnection) request.url().openConnection(request.proxy());
		}else{
			connection= (HttpURLConnection) request.url().openConnection();
		}
		connection.setRequestMethod(request.method().name());
		connection.setInstanceFollowRedirects(false); // don't rely on native redirection support
		connection.setConnectTimeout(request.timeout());
		connection.setReadTimeout(request.timeout());
		return connection;
	}
    
    /**
     * Initialise Trust manager that does not validate certificate chains and
     * add it to current SSLContext.
     * <p/>
     * please not that this method will only perform action if sslSocketFactory is not yet
     * instantiated.
     *
     * @throws IOException
     */
    private static synchronized void initUnSecureTSL() throws IOException {
        if (sslSocketFactory == null) {
        	sslSocketFactory=SslSocketFactoryBuilder.buildeNoValidateFactory();
        }
    }
    
    
    private static HostnameVerifier getInsecureVerifier() {
        return new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
    }
    
    private static String getRequestCookieString(HttpRequest<?> req) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        Set<Map.Entry<String, String>> cookies=req.cookies().entrySet();
        for (Map.Entry<String, String> cookie : cookies) {
            if (first){
            	first = false;
            }else{
            	sb.append("; ");
            }
            sb.append(cookie.getKey()).append('=').append(cookie.getValue());
            //todo: spec says only ascii, no escaping / encoding defined. validate on set? or escape somehow here?
        }
        return sb.toString();
    }
    
    /**
     * for get url reqs, serialise the data map into the url
     * @param req
     * @throws IOException
     */
    protected void serialiseRequestUrl(HttpRequest<?> req) throws IOException {
        URL in = req.url();
        StringBuilder url = new StringBuilder();
        boolean first = true;
        // reconstitute the query, ready for appends
        url.append(in.getProtocol())
            .append("://")
            .append(in.getAuthority()) // includes host, port
            .append(in.getPath())
            .append("?");
        if (in.getQuery() != null) {
            url.append(in.getQuery());
            first = false;
        }
        Collection<RequestValue> values=req.datas();
        for (RequestValue keyVal : values) {
        	KeyValue value=(KeyValue) keyVal;
            if (!first){
                url.append('&');
            }else{
                first = false;
            }
            url.append(URLEncoder.encode(value.name, req.charset()))
                .append('=')
                .append(URLEncoder.encode(value.value, req.charset()));
        }
        req.url(new URL(url.toString()));
        req.datas().clear(); // moved into url as get params
    }
    
    protected  String setOutputContentType(HttpRequest<?> req) {
        String bound = null;
        if (req.needsMultipart()) {
            bound = HttpDataUtils.mimeBoundary();
            req.header(CONTENT_TYPE, MULTIPART_FORM_DATA + "; boundary=" + bound);
        } else {
            req.header(CONTENT_TYPE, FORM_URL_ENCODED + "; charset=" + req.postDataCharset());
        }
        return bound;
    }
    /**
     * 提交post数据
     * @param req
     * @param outputStream
     * @param bound 多媒体上传绑定的key
     * @throws IOException
     */
    protected  void writePostDatas(final HttpRequest<?> req, final OutputStream outputStream, String bound) throws IOException {
    	Collection<RequestValue> data = req.datas();
        
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream, req.postDataCharset()));

        if (bound != null) {
            // boundary will be set if we're in multipart mode
            for (RequestValue keyVal : data) {
                w.write("--");
                w.write(bound);
                w.write("\r\n");
                w.write("Content-Disposition: form-data; name=\"");
                w.write(encodeMimeName(keyVal.name())); // encodes " to %22
                w.write("\"");
                if (keyVal.isStream()) {
                    w.write("; filename=\"");
                    w.write(encodeMimeName(keyVal.value()));
                    w.write("\"\r\nContent-Type: application/octet-stream\r\n\r\n");
                    w.flush(); // flush 缓冲
                    HttpDataUtils.crossStreams(keyVal.inputStream(), outputStream);
                    outputStream.flush();
                } else {
                    w.write("\r\n\r\n");
                    w.write(keyVal.value());
                }
                w.write("\r\n");
            }
            w.write("--");
            w.write(bound);
            w.write("--");
        } else if (req.requestBody() != null){
            // data will be in query string, we're sending a plaintext body
            w.write(req.requestBody());
        }else {
            // regular form data (application/x-www-form-urlencoded)
            boolean first = true;
            for (RequestValue keyVal: data) {
                if (!first){
                    w.append('&');
                }else{
                    first = false;
                }
                w.write(URLEncoder.encode(keyVal.name(), req.postDataCharset()));
                w.write('=');
                w.write(URLEncoder.encode(keyVal.value(), req.postDataCharset()));
            }
        }
        w.close();
    }
    
    protected String encodeMimeName(String val) {
        if (val == null){
            return null;
        }
        return val.replaceAll("\"", "%22");
    }
    
    protected  URL resolve(URL base, String relUrl) throws MalformedURLException {
        // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
        if (relUrl.startsWith("?")){
            relUrl = base.getPath() + relUrl;
        }
        // workaround: //example.com + ./foo = //example.com/./foo, not //example.com/foo
        if (relUrl.indexOf('.') == 0 && base.getFile().indexOf('/') != 0) {
            base = new URL(base.getProtocol(), base.getHost(), base.getPort(), "/" + base.getFile());
        }
        return new URL(base,relUrl);
    }
}
