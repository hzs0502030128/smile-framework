package org.smile.template;

import java.io.IOException;
import java.util.Map;

import org.smile.Smile;
import org.smile.collection.SoftHashMap;
import org.smile.commons.SmileRunException;
/**
 * 模板加载
 * @author 胡真山
 *
 */
public class Configuration {
	/**模板加载器*/
	private TemplateLoader loader;
	/**模板读取广西编码*/
	private String encode=Smile.ENCODE;
	/**创建的模板类型*/
	private String templateType=Template.SMILE;
	/**对模板对象的缓存*/
	private Map<String,TemplateData> cacheTemplates=SoftHashMap.newConcurrentInstance();
	
	public Configuration(TemplateLoader loader){
		this.loader=loader;
	}
	
	
	/**获取模板*/
	public Template getTemplate(String name){
		TemplateData templateData=cacheTemplates.get(name);
		Object templateSource=loader.getTemplateSource(name);
		if(templateSource==null){
			throw new NullPointerException("can not find template by name "+name);
		}
		long lastModify=loader.getLastModify(templateSource);
		if(templateData==null || templateData.lastModify<lastModify){
			String templateContent;
			try {
				templateContent = loader.getTemplateContent(templateSource, encode);
			} catch (IOException e) {
				throw new SmileRunException(e);
			}
			Template template=newTemplate(templateContent);
			templateData=new TemplateData(template, lastModify);
			cacheTemplates.put(name, templateData);
		}
		return templateData.template;
	}
	
	protected Template newTemplate(String templateContent){
		return new StringTemplate(this.templateType, templateContent);
	}
	
	class TemplateData{
		/**文本模板*/
		Template template;
		/**最后修改时间*/
		long lastModify;
		
		TemplateData(Template template,long lastModify){
			this.template=template;
			this.lastModify=lastModify;
		}
	}

	public void setTemplateLoader(TemplateLoader loader) {
		this.loader = loader;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
}
