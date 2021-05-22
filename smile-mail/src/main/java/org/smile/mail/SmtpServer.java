package org.smile.mail;


/**
 * Secure SMTP server (STARTTLS) for sending emails.
 */
public class SmtpServer extends AbstractSmtpServer<SmtpServer> {

	public static SmtpServer create(String host) {
		return new SmtpServer(host, DEFAULT_SMTP_PORT);
	}

	public static SmtpServer create(String host, int port) {
		return new SmtpServer(host, port);
	}

	public SmtpServer(String host) {
		super(host, DEFAULT_SMTP_PORT);
	}

	public SmtpServer(String host, int port) {
		super(host, port);
	}
}
