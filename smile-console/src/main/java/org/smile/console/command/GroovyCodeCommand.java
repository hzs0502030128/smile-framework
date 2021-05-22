package org.smile.console.command;

import org.smile.commons.ExceptionUtils;
import org.smile.console.config.CommandConfig;
import org.smile.console.context.ConsoleContext;
import org.smile.log.LoggerHandler;
import org.smile.script.ScriptExecutor;
import org.smile.script.ScriptType;

public class GroovyCodeCommand extends TemplateConsoleCommand implements LoggerHandler{
	
	public GroovyCodeCommand(CommandConfig config) {
		super(config);
	}

	@Override
	public void doCommand(ConsoleContext context) {
		String groovyCode=context.getParameter("groovyCode");
		context.setAttribute("groovyCode", groovyCode);
		ScriptExecutor executor=new  ScriptExecutor(ScriptType.GROOVY.value());
		try{
			Object result=executor.execute(groovyCode);
			context.setAttribute("result", result);
		}catch(Exception e){
			String exception=ExceptionUtils.throwableStack(e);
			context.setAttribute("exception", exception);
			logger.error(getCode(),e);
		}
		writeFtlToResp(context);
	}

	@Override
	public void initPage(ConsoleContext context) {
		writeFtlToResp(context);
	}

	@Override
	protected String fileExtension() {
		return ".stl.xml";
	}

}
