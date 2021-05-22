package org.smile.log.impl;

import java.util.logging.Filter;
import java.util.logging.LogRecord;
/**
 * 通过配置级别
 * 过滤是不是需要记录日志
 * @author 胡真山
 *
 */
public class JDKPackageLevelFilter implements Filter{
	@Override
	public boolean isLoggable(LogRecord record) {
		PackageSet packageSet=JDKLoggerFactory.config.getPackageSet(record.getLoggerName());
		if(packageSet==null){
			return true;
		}else{
			return packageSet.getLevel().getValue()<=JDKLogger.parseJdkLvl(record.getLevel()).getValue();
		}
	}
}
