package org.smile.log.impl;

import java.util.Collection;
import java.util.logging.Handler;

import org.smile.log.Logger;
import org.smile.util.SysUtils;

public class JDKLoggerFactory extends AbstractLoggerFactory {
	
	protected static final JDKSmileLogConfig config=new JDKSmileLogConfig();
	
	static {
		try {
			config.loadConfig();
		} catch (Exception e) {
			SysUtils.println("初始化日志失败" + e.getMessage());
		}
	}


	@Override
	protected Logger newInstanceLogger(String name) {
		JDKLogger jdkLog = new JDKLogger(java.util.logging.Logger.getLogger(name));
		jdkLog.setLevel(config.getLevel());
		try {
			PackageSet set=config.getPackageSet(name);//设置包结构级别
			if(set!=null){
				jdkLog.setLevel(set.getLevel());
			}
			// 否则读取smile中的配置级别和handler处理类
			Collection<Handler> handlers = config.getHandlers(name,set);
			//不使用父日志处理
			jdkLog.logger.setUseParentHandlers(false);
			//添加处理器
			for (Handler handler : handlers) {
				jdkLog.logger.addHandler(handler);
			}
		} catch (Exception e) {
			SysUtils.println("设置logger级别出错", e);
		}
		return jdkLog;
	}
}