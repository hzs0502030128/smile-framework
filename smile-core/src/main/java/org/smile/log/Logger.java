package org.smile.log;

/**
 * Simple Logger interface. It defines only logger methods with string
 * argument as our coding style and approach insist in always using if block
 * around the logging.
 */
public interface Logger {

	/**
	 * Logger level.
	 */
	public enum Level {
		/**痕迹*/
		TRACE(1),
		DEBUG(2),
		INFO(3),
		WARN(4),
		ERROR(5),
		/**打印*/
		PRINT(6);
		
		private final int value;
		
		Level(int value) {
			this.value = value;
		}
		
		public int getValue(){
			return value;
		}

		/**
		 * Returns <code>true</code> if this level
		 * is enabled for given required level.
		 */
		public boolean isEnabledFor(Level level) {
			return this.value >= level.value;
		}
		/**
		 * 以value获取枚举
		 * @param lvl
		 * @return
		 */
		public static Level valueOf(int lvl){
			for(Level lv:values()){
				if(lvl==lv.value){
					return lv;
				}
			}
			return null;
		}
		
		public static Level toLevel(String lvl){
			for(Level lv:values()){
				if(lv.name().equalsIgnoreCase(lvl)){
					return lv;
				}
			}
			return null;
		}
	}

	/**
	 * Returns Logger name.
	 */
	public String getName();

	/**
	 * Returns <code>true</code> if certain logging
	 * level is enabled.
	 */
	public boolean isEnabled(Level level);

	/**
	 * Logs a message at provided logging level.
	 */
	public void log(Level level, Object message);



	/**
	 * Returns <code>true</code> if TRACE level is enabled.
	 */
	public boolean isTraceEnabled();

	/**
	 * Logs a message at TRACE level.
	 */
	public void trace(Object message);


	/**
	 * Returns <code>true</code> if DEBUG level is enabled.
	 */
	public boolean isDebugEnabled();

	/**
	 * Logs a message at DEBUG level.
	 */
	public void debug(Object message);
	
	/**
	 * Logs a message at DEBUG level.
	 */
	public void debug(String message,Throwable throwable);

	/**
	 * Returns <code>true</code> if INFO level is enabled.
	 */
	public boolean isInfoEnabled();

	/**
	 * Logs a message at INFO level.
	 */
	public void info(Object message);
	
	/**
	 * Logs a message at INFO level.
	 */
	public void info(String message,Throwable throwable);


	/**
	 * Returns <code>true</code> if WARN level is enabled.
	 */
	public boolean isWarnEnabled();

	/**
	 * Logs a message at WARN level.
	 */
	public void warn(Object message);

	/**
	 * Logs a message at WARN level.
	 */
	public void warn(String message, Throwable throwable);


	/**
	 * Returns <code>true</code> if ERROR level is enabled.
	 */
	public boolean isErrorEnabled();

	/**
	 * Logs a message at ERROR level.
	 */
	public void error(Object message);

	/**
	 * Logs a message at ERROR level.
	 */
	public void error(String message, Throwable throwable);
	/**
	 * Logs a message at ERROR level.
	 * @param throwable
	 */
	public void error(Throwable throwable);
	/***
	 * 打印
	 * @param message
	 * @param throwable
	 */
	public void print(String message, Throwable throwable);
	/**
	 * 
	 * @param throwable
	 */
	public void print(Throwable throwable);
	
	/**
	 * 
	 * @param message
	 */
	public void print(Object message);
}
