package org.smile.json.parser;

import org.smile.commons.SmileException;

/**
 * JSON解析异常
 * @author huzhenshan hzs0502030128@163.com
 *
 */
public class JSONParseException extends SmileException {
	
	public JSONParseException(String message) {
		super("Parse Json String Case a Exception,"+message);
	}

	public JSONParseException(Throwable cause) {
		super(cause);
	}
}
