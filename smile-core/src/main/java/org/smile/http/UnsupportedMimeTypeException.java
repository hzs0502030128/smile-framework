package org.smile.http;

import org.smile.io.IOException;
/**
 * 不支持的资源类型异常
 * @author 胡真山
 *
 */
public class UnsupportedMimeTypeException extends IOException {
	
    private String mimeType;
    
    private String url;

    public UnsupportedMimeTypeException(String message, String mimeType, String url) {
        super(message);
        this.mimeType = mimeType;
        this.url = url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return super.toString() + ". Mimetype=" + mimeType + ", URL="+url;
    }
}
