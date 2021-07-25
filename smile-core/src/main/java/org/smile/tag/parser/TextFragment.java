package org.smile.tag.parser;

import org.smile.commons.NotImplementedException;
import org.smile.tag.Fragment;
import org.smile.tag.TagContext;
import org.smile.tag.TagEngine;
import org.smile.tag.impl.TagException;
import org.smile.template.TemplateParser;
import org.smile.template.handler.WrapSimpleTemplateHandler;
import org.smile.util.Entities;

import java.io.IOException;
import java.io.Writer;

public class TextFragment implements Fragment{
	/**
	 * 文本片断模板
	 */
	protected WrapSimpleTemplateHandler templateHandler;
	
	public TextFragment(CharSequence text){
		//处理转义符
		String textContent =Entities.XML.unescape(text.toString());
		templateHandler =new WrapSimpleTemplateHandler(textContent);
	}

	@Override
	public void addSub(Fragment sub) {
		throw new NotImplementedException("not support add sub on text fragment");
	}

	@Override
	public void invoke(TagContext tagContext) throws TagException {
		TagEngine tagEngine=tagContext.getTagEngine();
		String content= templateHandler.processToString(tagEngine.expressionEngine(),tagContext);
		try {
			Writer writer=tagContext.getWriter();
			writer.write(content);
		} catch (IOException e) {
			throw new TagException(e);
		}
	}

	public String getText(){
		return templateHandler.getTemplateTxt();
	}

	@Override
	public String toString() {
		return templateHandler.getTemplateTxt();
	}

	public TemplateParser.Fragment parse(){
		return templateHandler.fragment();
	}

	@Override
	public boolean isTag() {
		return false;
	}
}
