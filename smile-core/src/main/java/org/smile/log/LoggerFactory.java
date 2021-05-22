package org.smile.log;

import org.smile.Smile;
import org.smile.log.impl.JCLLoggerFactory;
import org.smile.log.impl.JDKLoggerFactory;
import org.smile.log.impl.Log4jLoggerFactory;
import org.smile.log.impl.NOPLoggerFactory;
import org.smile.log.impl.SimpleLoggerFactory;
import org.smile.log.impl.Slf4jLoggerFactory;
import org.smile.util.SysUtils;
/**
 * Logger factory.
 */
public final class LoggerFactory{
	
	private static LoggerFactoryInterface loggerFactory = new NOPLoggerFactory();
	
	private static volatile boolean init;
	
	static {
		synchronized(LoggerFactory.class){
			try {
				Class<LoggerFactoryInterface> factoryType = Smile.config.getValue(Smile.LOG_FACTORY_CONFIG_KEY, Class.class);
				if (factoryType != null) {
					tryLoggerFactoryImple(factoryType);
				}
				tryImplementsLoggers();
			} catch (Throwable e) {
				SysUtils.println("初始化日志工厂失败", e); 
			}
		}
	}
	
	
	/**
	 * 尝试所有的logger
	 */
	protected static void tryImplementsLoggers(){
		tryLoggerFactoryImple(Log4jLoggerFactory.class);
		tryLoggerFactoryImple(Slf4jLoggerFactory.class);
		tryLoggerFactoryImple(SimpleLoggerFactory.class);
		tryLoggerFactoryImple(JCLLoggerFactory.class);
		tryLoggerFactoryImple(JDKLoggerFactory.class);
	}
	
	private static void tryLoggerFactoryImple(Class<? extends LoggerFactoryInterface> factoryType){
		if(init){//初始化
			return;
		}
		try{
			LoggerFactoryInterface factory=factoryType.newInstance();
			Logger logger=factory.getLogger(LoggerFactory.class.getName());
			logger.info("初始化"+factoryType+"成功");
			loggerFactory=factory;
			init=true;
			SysUtils.println(" 尝试 "+factoryType+"成功");
		}catch(Throwable e){
			SysUtils.println(" 尝试 "+factoryType+"失败"+e.getMessage());
		}
	}
	
	/**
	 * Sets logger factory implementation.
	 */
	public static void setLoggerFactory(LoggerFactoryInterface loggerFactoryInterface) {
		loggerFactory = loggerFactoryInterface;
	}

	/**
	 * Returns logger for given class.
	 */
	public static Logger getLogger(Class clazz) {
		return getLogger(clazz.getName());
	}

	/**
	 * Returns logger for given name.
	 */
	public static Logger getLogger(String name) {
		return loggerFactory.getLogger(name);
	}

}