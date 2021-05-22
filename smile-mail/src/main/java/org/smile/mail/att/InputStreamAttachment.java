// Copyright (c) 2003-present, Jodd Team (jodd.org). All Rights Reserved.

package org.smile.mail.att;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.smile.mail.EmailAttachment;
import org.smile.mail.MailException;

/**
 * <code>InputStream</code> {@link EmailAttachment email attachment}.
 */
public class InputStreamAttachment extends EmailAttachment {

	protected final InputStream inputStream;
	protected final String contentType;

	public InputStreamAttachment(InputStream inputStream, String contentType, String name, String contentId) {
		super(name, contentId);
		this.inputStream = inputStream;
		this.contentType = contentType;
	}

	/**
	 * Returns <code>ByteArrayDataSource</code>.
	 */
	@Override
	public DataSource getDataSource() {
		try {
			return new ByteArrayDataSource(inputStream, contentType);
		} catch (IOException ioex) {
			throw new MailException(ioex);
		}
	}

	/**
	 * Returns content type.
	 */
	public String getContentType() {
		return contentType;
	}

}