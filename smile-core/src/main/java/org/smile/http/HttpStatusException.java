package org.smile.http;
import java.io.IOException;

public class HttpStatusException extends IOException {
	/**响应状态*/
    private int statusCode;
    /**请求的URL*/
    private String url;

    public HttpStatusException(String message, int statusCode, String url) {
        super(message);
        this.statusCode = statusCode;
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getUrl() {
        return url;
    }
    
    @Override
    public String getMessage() {
    	return super.getMessage()+",status="+statusCode+",url="+url;
    }

    @Override
    public String toString() {
        return super.toString() + ". Status=" + statusCode + ", URL=" + url;
    }
}