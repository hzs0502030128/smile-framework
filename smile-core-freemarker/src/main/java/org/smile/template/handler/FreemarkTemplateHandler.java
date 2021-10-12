package org.smile.template.handler;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Locale;

import org.smile.Smile;
import org.smile.template.IncludesContext;
import org.smile.template.TemplateHandlerException;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkTemplateHandler extends AbstractTemplateHandler implements TemplateLoader{

	private Configuration cfg =new Configuration();
	
	protected static final String DEFAULT_NAME="STRING_TEMPLATE";
	
	private StringReader reader;
	
	private Template template;
	
	private IncludesContext includes;

	public FreemarkTemplateHandler(String templateText){
		this.templateTxt=templateText;
		this.reader=new StringReader(templateText);
		cfg.setTemplateLoader(this);
		cfg.setDefaultEncoding(Smile.ENCODE);
		try {
			template= cfg.getTemplate(DEFAULT_NAME);
		} catch (Exception e) {
			throw new TemplateHandlerException("process template error,content "+templateText, e);
		}
	}
	
	public void closeTemplateSource(Object templateSource) throws IOException {
		
	}
	/***
	 * 获取模板的真实名称
	 * @param name
	 * @return
	 */
	protected String getRealTemplateName(String name){
		String local=Locale.getDefault().toString();
		return name.replace("_"+local, "");
	}

	public Object findTemplateSource(String name) throws IOException {
		name=getRealTemplateName(name);
		if(name.startsWith(DEFAULT_NAME)){
			return reader;
		}else{
			if(includes==null){
				throw new IOException("不存在的模板:"+name);
			}
			Reader reader=includes.getInclude(name);
			if(reader==null){
				throw new IOException("不存在的include模板:"+name);
			}
			return reader;
		}
	}

	public long getLastModified(Object templateSource) {
		return 0;
	}

	public Reader getReader(Object templateSource, String encoding)
			throws IOException {
		return (Reader) templateSource;
	}

	@Override
	public void parse(Object params, Writer out) {
		try {
			template.process(params, out);
		} catch (Exception e) {
			throw new TemplateHandlerException("process template error,content "+reader+" params "+params, e);
		}
	}

	@Override
	public void setIncludeContext(IncludesContext context) {
		this.includes=context;
	}

}
