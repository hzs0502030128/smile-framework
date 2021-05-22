package org.smile.db.parser;

import org.smile.commons.SmileRunException;

public class SqlParserException extends SmileRunException {

	public SqlParserException(String msg, Throwable e) {
		super(msg, e);
	}

}
