package org.smile.log;

/**
 * Adapter for various logger implementations.
 */
public interface LoggerFactoryInterface {

	/**
	 * Returns {@link org.smile.log.Logger} for given name.
	 */
	Logger getLogger(String name);

}