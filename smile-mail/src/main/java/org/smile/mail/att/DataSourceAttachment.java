// Copyright (c) 2003-present, Jodd Team (jodd.org). All Rights Reserved.

package org.smile.mail.att;

import javax.activation.DataSource;

import org.smile.mail.EmailAttachment;

/**
 * Generic <code>DataSource</code> adapter for attachments.
 */
public class DataSourceAttachment extends EmailAttachment {

	protected final DataSource dataSource;

	public DataSourceAttachment(DataSource dataSource, String name, String contentId) {
		super(name, contentId);
		this.dataSource = dataSource;
	}

	/**
	 * Returns wrapped data source.
	 */
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}
}
