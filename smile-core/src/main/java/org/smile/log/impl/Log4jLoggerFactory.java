package org.smile.log.impl;

import org.smile.log.Logger;

public class Log4jLoggerFactory extends AbstractLoggerFactory{

	@Override
	protected Logger newInstanceLogger(String name) {
		return new Log4jLogger(org.apache.log4j.Logger.getLogger(name));
	}
}