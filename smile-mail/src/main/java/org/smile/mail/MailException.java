// Copyright (c) 2003-present, Jodd Team (jodd.org). All Rights Reserved.

package org.smile.mail;

import org.smile.commons.SmileRunException;

/**
 * Mailing exception.
 */
public class MailException extends SmileRunException{

	public MailException(String message) {
		super(message);
	}

	public MailException(String message, Throwable t) {
		super(message, t);
	}

	public MailException(Throwable t) {
		super(t);
	}
}
