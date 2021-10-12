package org.smile.template;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyHandlers;
import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.function.Function;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
/**
 * 可以自定义函数的freemarker模板
 * 
 * 如需要在模板中使用函数  ${fn(name,age)}
 * 
 * st.registFunction(new Function() {
 			@Override
			public Object getFunctionValue(Object... args) {
				return args[0]+"-"+args[1];
			}
			
			@Override
			public String getFunctionName() {
				return "fn";
			}
		});
 * @author 胡真山
 *
 */
public class WrapFreemakerStringTemplate extends FreemarkStringTemplate implements WrapTemplate{
	/**
	 * 函数信息
	 */
	private Map<String, TemplateMethodModelEx> functionMap=new HashMap<String, TemplateMethodModelEx>();

	public WrapFreemakerStringTemplate(String templateTxt) {
		super(templateTxt);
	}

	@Override
	public void process(Object params, Writer out) {
		if (CollectionUtils.notEmpty(functionMap)) {
			final Object paramsObj = params;
			TemplateHashModel model = new TemplateHashModel() {
				private DefaultObjectWrapper wrapper = new DefaultObjectWrapper();
				PropertyHandler propertyHandler=PropertyHandlers.getHanlder(paramsObj.getClass());
				@Override
				public TemplateModel get(String key) throws TemplateModelException {
					Object value = functionMap.get(key);
					if (value == null) {
						try {
							value = propertyHandler.getExpFieldValue(paramsObj, key);
							if(value==null){
								value= "";
							}
						} catch (BeanException e) {
							throw new SmileRunException(e);
						}
					}
					return wrapper.wrap(value);
				}
				@Override
				public boolean isEmpty() throws TemplateModelException {
					return false;
				}
			};
			super.process(model, out);
		} else {
			super.process(params, out);
		}
	}
	
	
	@Override
	public String processToString(Object params) {
		Writer result=new StringWriter((int)(templateTxt.length()*1.5));
		process(params, result);
		return result.toString();
	}

	/**
	 * 注册一个函数
	 * @param f
	 */
	@Override
	public void registFunction(final Function f) {
		functionMap.put(f.getName(), new TemplateMethodModelEx(){
			@Override
			public Object exec(List args) throws TemplateModelException {
				return f.getFunctionValue(args.toArray());
			}
		});
	}

	@Override
	public void registFunctions(Collection<Function> fs) {
		for(Function f:fs){
			registFunction(f);
		}
	}

}
