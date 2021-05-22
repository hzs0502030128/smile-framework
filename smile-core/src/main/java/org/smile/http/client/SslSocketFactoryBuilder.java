package org.smile.http.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SslSocketFactoryBuilder {
	
	/**
	 * 创建一个不需要验证证书链的信任
	 * @return
	 * @throws IOException
	 */
	public static SSLSocketFactory buildeNoValidateFactory() throws IOException{
		// Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
            }

            public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Can't create unsecure trust manager");
        } catch (KeyManagementException e) {
            throw new IOException("Can't create unsecure trust manager");
        }
	}
}
