package org.smile.template.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.Smile;
import org.smile.commons.SmileRunException;
import org.smile.file.ClassPathFileScaner;
import org.smile.file.ClassPathFileScaner.BaseVisitor;
import org.smile.template.Template;
import org.smile.template.handler.NoneTemplateHandler;
import org.smile.template.handler.SimpleTemplateHandler;
import org.smile.template.handler.SmileTemplateHandler;
import org.smile.template.handler.TemplateHandler;
import org.smile.template.handler.WrapSimpleTemplateHandler;
import org.smile.util.Properties;
import org.smile.util.SysUtils;
/**
 * 模板解析适配器
 * @author 胡真山
 * 2015年12月7日
 */
public class TemplateAdapterContext {
	/**默认的单例*/
	protected static TemplateAdapterContext instance=new TemplateAdapterContext(); 
	
	protected Map<String ,TemplateAdapter> adapters=new ConcurrentHashMap<String ,TemplateAdapter>();
	/**模板适配器配置文件名称*/
	private static final String ADAPTER_CONFIG_EXT=".template.adapter";
	/**模板适配器配置文件目录*/
	private static final String ADAPTER_CONFIG_DIR="META-INF/template/";
	
	static{
		//加载外部适配的模板适配器
		ClassPathFileScaner scaner=new ClassPathFileScaner(ADAPTER_CONFIG_DIR,new BaseVisitor() {
			@Override
			public boolean visit(String fileName,InputStream is) throws IOException {
				Properties p=new Properties();
				p.load(is);
				for(Object s:p.keySet()){
					try {
						Class.forName(String.valueOf(s));
					} catch (ClassNotFoundException e) {
						SysUtils.log(e);
					}
				}
				return false;
			}
			
			@Override
			public boolean accept(String fileName, String protocol) {
				return fileName.endsWith(ADAPTER_CONFIG_EXT);
			}
		});
		try {
			scaner.scanning();
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}
	
	public TemplateAdapterContext(){
		adapters.put(Template.SIMPLE, new SmileTemplateAdapter(SimpleTemplateHandler.class));
		adapters.put(Template.NONE, new SmileTemplateAdapter(NoneTemplateHandler.class));
		adapters.put(Template.EXPRESSION, new SmileTemplateAdapter(WrapSimpleTemplateHandler.class));
		adapters.put(Template.SMILE, new SmileTemplateAdapter(SmileTemplateHandler.class));
	}
	
	public void registAdapter(String type,TemplateAdapter adapter){
		adapters.put(type, adapter);
	}
	/***
	 * 实例化一个模板处理器
	 * @param type 模板类型
	 * @param templateText
	 * @return
	 */
	public final TemplateHandler newTemplateHandler(String type,String templateText){
		if(type==null){
			//默认为模板类型
			type=Smile.config.getProperty(Smile.TEMPLATE_TYPE_DEFAULT_KEY, Template.SMILE);
		}
		TemplateAdapter adapter=adapters.get(type);
		if(adapter==null){
			throw new SmileRunException("未知的模板类型:"+type);
		}
		return adapter.newInstance(templateText);
	}
	
	public static TemplateAdapterContext getInstance(){
		return instance;
	}
}
