// Copyright (c) 2003-present, Jodd Team (jodd.org). All Rights Reserved.

package org.smile.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * IMAP Server.
 */
public class ImapServer implements ReceiveMailSessionProvider {

	protected static final String MAIL_IMAP_PORT = "mail.imap.port";
	protected static final String MAIL_IMAP_HOST = "mail.imap.host";

	protected static final String PROTOCOL_IMAP = "imap";

	protected static final int DEFAULT_IMAP_PORT = 143;

	protected final String host;
	protected final int port;
	protected final Authenticator authenticator;
	protected final Properties sessionProperties;

	/**
	 * POP3 server defined with its host and default port.
	 */
	public ImapServer(String host) {
		this(host, DEFAULT_IMAP_PORT, null);
	}
	/**
	 * POP3 server defined with its host and port.
	 */
	public ImapServer(String host, int port) {
		this(host, port, null);
	}

	public ImapServer(String host, Authenticator authenticator) {
		this(host, DEFAULT_IMAP_PORT, authenticator);
	}

	public ImapServer(String host, int port, String username, String password) {
		this(host, port, new SimpleAuthenticator(username, password));
	}

	/**
	 * SMTP server defined with its host and authentication.
	 */
	public ImapServer(String host, int port, Authenticator authenticator) {
		this.host = host;
		this.port = port;
		this.authenticator = authenticator;
		sessionProperties = createSessionProperties();
	}

	/**
	 * Prepares mail session properties.
	 */
	protected Properties createSessionProperties() {
		Properties props = new Properties();
		props.setProperty(MAIL_IMAP_HOST, host);
		props.setProperty(MAIL_IMAP_PORT, String.valueOf(port));
		return props;
	}


	/**
	 * {@inheritDoc}
	 */
	public ReceiveMailSession createSession() {
		Session session = Session.getInstance(sessionProperties, authenticator);
		Store store;
		try {
			store = getStore(session);
		} catch (NoSuchProviderException nspex) {
			throw new MailException("Failed to create IMAP session", nspex);
		}
		return new ReceiveMailSession(session, store);
	}

	/**
	 * Returns email store.
	 */
	protected Store getStore(Session session) throws NoSuchProviderException {
		return session.getStore(PROTOCOL_IMAP);
	}

	// ---------------------------------------------------------------- getters

	/**
	 * Returns POP host address.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Returns authenticator.
	 */
	public Authenticator getAuthenticator() {
		return authenticator;
	}

	/**
	 * Returns current port.
	 */
	public int getPort() {
		return port;
	}
}