package org.smile.log.jvm;

import java.util.HashMap;
import java.util.Map;

import org.smile.Smile;
import org.smile.collection.CollectionUtils;
import org.smile.commons.ExceptionUtils;
import org.smile.console.command.TemplateConsoleCommand;
import org.smile.console.config.CommandConfig;
import org.smile.console.context.ConsoleContext;
import org.smile.json.JSONFormatter;
import org.smile.json.JSONObject;
import org.smile.util.StringUtils;
/**
 * 
 * @author 胡真山
 * @date 2018年3月2日
 *
 */
public class JvmInfoConsoleCommand extends TemplateConsoleCommand{
	
	protected static final String JVM_METHOD_PARAM="jvmMethod";
	protected static final String JVM_SYSTEM_PARAM="system";

	public JvmInfoConsoleCommand(CommandConfig config) {
		super(config);
	}

	@Override
	public void doCommand(ConsoleContext context) {
		String jvmMethod=context.getParameter(JVM_METHOD_PARAM);
		if(StringUtils.isEmpty(jvmMethod)){
			initPage(context);
		}else if(JVM_SYSTEM_PARAM.equals(jvmMethod)){
			Map<Object,Object> systemInfo=new HashMap<Object,Object>();
			CollectionUtils.putAll(systemInfo, System.getProperties(), CollectionUtils.hashSet(Smile.HOT_INSTRUMENTATION_KEY));
			String json=JSONObject.toJSONString(systemInfo);
			context.setAttribute("result", json);
			writeFtlToResp(context);
		}else{
			try {
				Object result=JVMLogger.class.getMethod(jvmMethod).invoke(null);
				context.setAttribute("result", result);
			} catch (Exception e) {
				context.setAttribute("exception", ExceptionUtils.throwableStack(e));
				logger.error(e);
			}
			writeFtlToResp(context);
		}
	}

	@Override
	public void initPage(ConsoleContext context) {
		writeFtlToResp(context);
	}

}
