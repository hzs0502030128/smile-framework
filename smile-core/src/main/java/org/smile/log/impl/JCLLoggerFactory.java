package org.smile.log.impl;

import org.smile.log.Logger;
import org.smile.log.LoggerFactoryInterface;

public class JCLLoggerFactory implements LoggerFactoryInterface {
	public Logger getLogger(String name) {
		return new JCLLogger(org.apache.commons.logging.LogFactory.getLog(name));
	}
}