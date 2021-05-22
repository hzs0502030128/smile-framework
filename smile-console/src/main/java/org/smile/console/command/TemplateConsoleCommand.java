package org.smile.console.command;

import java.io.IOException;
import java.io.InputStream;

import org.smile.commons.SmileRunException;
import org.smile.console.config.CommandConfig;
import org.smile.console.context.ConsoleContext;
import org.smile.io.IOUtils;
import org.smile.log.LoggerHandler;
import org.smile.template.StringTemplate;
import org.smile.template.Template;
import org.smile.util.RegExp;


public abstract class TemplateConsoleCommand extends AbstractCommand implements LoggerHandler{
	/**用于把类名的点转成斜杠*/
	protected RegExp replaceExp=new RegExp("\\.");
	/**模板信息*/
	protected Template template;
	
	public TemplateConsoleCommand(CommandConfig config) {
		super(config);
		try {
			template=initCommandFtl();
		} catch (IOException e) {
			throw new SmileRunException("init template "+getTemplatePath()+" error ",e);
		}
	}
	
	protected String getTemplatePath(){
		return replaceExp.replaceAll(getClass().getName(),"/")+fileExtension();
	}
	/***
	 * 模板文件扩展名
	 * @return
	 */
	protected  String fileExtension(){
		return ".stl.xml";
	}
	/**
	 * 使用模板类型
	 * @return
	 */
	protected String templateType(){
		return Template.SMILE;
	}
	
	protected Template initCommandFtl() throws IOException{
		String text=IOUtils.readString(getCommandFtlInput());
		return new StringTemplate(templateType(), text);
	}
	
	protected InputStream getCommandFtlInput(){
		return getClass().getClassLoader().getResourceAsStream(getTemplatePath());
	}
	
	protected void writeFtlToResp(ConsoleContext context){
		context.writeReponse(template.processToString(context));
	}

	@Override
	public void init() {
		
	}
	
	
}
