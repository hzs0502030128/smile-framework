package org.smile.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class DefaultHttpResponse extends AbstractHttpResponse<ByteBuffer> {
	
	protected ByteBuffer byteContent;

	@Override
	public void recieveContent(InputStream is,int maxBodySize) throws IOException {
		byteContent= HttpDataUtils.readToByteBuffer(is, maxBodySize);
	}
	
	@Override
	public String readBody() throws UnsupportedEncodingException {
		return new String(byteContent.array(),charset);
	}
	
	@Override
	public ByteBuffer getContent() {
		return byteContent.duplicate();
	}
}
