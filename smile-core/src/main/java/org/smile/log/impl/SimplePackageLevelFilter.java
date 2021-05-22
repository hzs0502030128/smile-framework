package org.smile.log.impl;

import org.smile.log.record.HandleFilter;
import org.smile.log.record.LogRecord;
/**
 * 通过配置级别
 * 过滤是不是需要记录日志
 * @author 胡真山
 *
 */
public class SimplePackageLevelFilter implements HandleFilter{
	@Override
	public boolean isLoggable(LogRecord record) {
		PackageSet packageSet=SimpleLoggerFactory.config.getPackageSet(record.getClassName());
		if(packageSet==null){
			return true;
		}else{
			return record.getLevel().isEnabledFor(packageSet.getLevel());
		}
	}
}
