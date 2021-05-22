// Copyright (c) 2003-present, Jodd Team (jodd.org). All Rights Reserved.

package org.smile.mail;

import java.util.Properties;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.smile.commons.Strings;

import com.sun.mail.pop3.POP3SSLStore;

/**
 * POP3 SSL server.
 */
public class Pop3SslServer extends Pop3Server {

	protected static final String MAIL_POP3_SOCKET_FACTORY_PORT 	= "mail.pop3.socketFactory.port";
	protected static final String MAIL_POP3_SOCKET_FACTORY_CLASS 	= "mail.pop3.socketFactory.class";
	protected static final String MAIL_POP3_SOCKET_FACTORY_FALLBACK = "mail.pop3.socketFactory.fallback";
	protected static final int DEFAULT_SSL_PORT = 995;

	protected final String username;
	protected final String password;

	public Pop3SslServer(String host, String username, String password) {
		this(host, DEFAULT_SSL_PORT, username, password);
	}

	public Pop3SslServer(String host, int port, String username, String password) {
		super(host, port, username, password);
		this.username = username;
		this.password = password;
	}


	@Override
	protected Properties createSessionProperties() {
		Properties props = new Properties();
		props.setProperty(MAIL_POP3_PORT, String.valueOf(port));
		props.setProperty(MAIL_POP3_SOCKET_FACTORY_PORT, String.valueOf(port));
		props.setProperty(MAIL_POP3_SOCKET_FACTORY_CLASS, "javax.net.ssl.SSLSocketFactory");
		props.setProperty(MAIL_POP3_SOCKET_FACTORY_FALLBACK, Strings.FALSE);
		return props;
	}

	@Override
	protected Store getStore(Session session) throws NoSuchProviderException {
		URLName url = new URLName(PROTOCOL_POP3, host, port, "", username, password);
		return new POP3SSLStore(session, url);
	}
}