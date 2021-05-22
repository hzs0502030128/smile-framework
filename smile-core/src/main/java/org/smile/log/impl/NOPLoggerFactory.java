package org.smile.log.impl;

import org.smile.log.Logger;
import org.smile.log.LoggerFactoryInterface;

public class NOPLoggerFactory implements LoggerFactoryInterface {

	private NOPLogger logger = new NOPLogger("*");
	/**
	 * {@inheritDoc}
	 */
	public Logger getLogger(String name) {
		return logger;
	}
}