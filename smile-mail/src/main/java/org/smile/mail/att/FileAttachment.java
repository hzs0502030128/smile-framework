// Copyright (c) 2003-present, Jodd Team (jodd.org). All Rights Reserved.

package org.smile.mail.att;

import java.io.File;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.smile.mail.EmailAttachment;

/**
 * File {@link EmailAttachment email attachment}.
 * Content type is not set by user, but by <code>javax.mail</code>
 * framework.
 */
public class FileAttachment extends EmailAttachment {

	protected final File file;

	public FileAttachment(File file, String name, String contentId) {
		super(name, contentId);
		this.file = file;
	}

	/**
	 * Returns attached file.
	 */
	public File getFile() {
		return file;
	}

	@Override
	public DataSource getDataSource() {
		return new FileDataSource(file);
	}
}
