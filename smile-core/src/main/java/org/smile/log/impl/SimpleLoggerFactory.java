package org.smile.log.impl;

import java.util.Collection;

import org.smile.commons.ExceptionUtils;
import org.smile.log.Logger;
import org.smile.log.record.Handler;
import org.smile.util.SysUtils;

/**
 * 简单日志类型工厂的一样实现 打印在控制台上
 * @author 胡真山
 *
 */
public class SimpleLoggerFactory extends  AbstractLoggerFactory{
	
	protected static final SimpleSmileLogConfig config=new SimpleSmileLogConfig();
	
	static {
		try {
			//初始化配置文件
			config.loadConfig();
		} catch (Exception e) {
			SysUtils.log("初始化日志失败:" +ExceptionUtils.getExceptionMsg(e));
		}
	}

	@Override
	protected Logger newInstanceLogger(String name) {
		SimpleLogger jdkLog = new SimpleLogger(name);
		jdkLog.setLevel(config.getLevel());
		try {
			PackageSet set=config.getPackageSet(name);//设置包结构级别
			if(set!=null){
				jdkLog.setLevel(set.getLevel());
			}
			// 否则读取smile中的配置级别和handler处理类
			Collection<Handler> handlers = config.getHandlers(name,set);
			jdkLog.setHandler(handlers);
		} catch (Exception e) {
			SysUtils.println("设置logger级别出错", e);
		}
		return jdkLog;
	}
}