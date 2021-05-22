package org.smile.strate.jump.adapter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyHandlers;
import org.smile.beans.converter.BeanException;
import org.smile.commons.Strings;
import org.smile.io.IOUtils;
import org.smile.strate.Strate;
import org.smile.strate.action.Action;
import org.smile.strate.action.ActionContext;
import org.smile.strate.action.ActionElement;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.jump.JumpHandler;
import org.smile.strate.jump.StrateResultJumpException;
import org.smile.template.TemplateHandlerException;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
/**
 * 用于对freemarker的一个适配 
 * 当没有引入freemarker包时不影响期它类型正常使用
 * @author 胡真山
 * @Date 2016年2月1日
 */
public class FreemarkerAdapter implements JumpHandler{
	
	private static Configuration cfg = new Configuration();
	
	static{
		String baseDir = ActionContext.getServletContext().getRealPath("/");
		FileTemplateLoader loader;
		try {
			loader = new FileTemplateLoader(new File(baseDir));
		} catch (IOException e) {
			throw new TemplateHandlerException(e);
		}
		cfg.setTemplateLoader(loader);
		cfg.setDefaultEncoding(Strate.encoding);
	}

	@Override
	public void jump(DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		jump(new AttributeModel(context.getAction()),context, forward);
	}

	@Override
	public void jump(Object methodResult, DispatchContext context, ResultConfig forward) throws StrateResultJumpException{
		Writer writer=null;
		try {
			Template template= cfg.getTemplate(forward.getValue());
			writer= new OutputStreamWriter(context.getResponse().getOutputStream(), Strate.encoding);
			template.process(methodResult, writer);
			writer.flush();
		} catch (Exception e) {
			throw new StrateResultJumpException("process template error," + forward.getName() + "-" + forward.getValue(), e);
		}finally{
			IOUtils.close(writer);
		}
	}
	/**
	 * 对action进行包装
	 * 用于可以对request中的attribute中内容可获取
	 * @author 胡真山
	 *
	 */
	class AttributeModel implements  TemplateHashModel{
		private DefaultObjectWrapper wrapper = new DefaultObjectWrapper();
		PropertyHandler propertyHandler;
		Action action;
		AttributeModel(Action action){
			this.action=action;
			this.propertyHandler=PropertyHandlers.getHanlder(action.getClass(),false);
		}
		@Override
		public TemplateModel get(String key) throws TemplateModelException {
			try {
				Object value = propertyHandler.getExpFieldValue(action, key);
				if (value == null) {
					value = action.request().getAttribute(key);
					if(value==null){
						value= Strings.BLANK;
					}
				}
				return wrapper.wrap(value);
			} catch (BeanException e) {
				throw new TemplateModelException(e);
			}
		}
		@Override
		public boolean isEmpty() throws TemplateModelException {
			return false;
		}
	}
}
