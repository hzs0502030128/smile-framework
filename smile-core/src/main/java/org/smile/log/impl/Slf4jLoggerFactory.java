package org.smile.log.impl;

import org.smile.log.Logger;

public class Slf4jLoggerFactory extends AbstractLoggerFactory{

	@Override
	protected Logger newInstanceLogger(String name) {
		return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(name));
	}
}