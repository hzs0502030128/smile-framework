package org.smile.script;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyHandlers;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.commons.SmileRunException;
import org.smile.template.SimpleStringTemplate;
import org.smile.template.Template;
import org.smile.util.Properties;
import org.smile.util.StringUtils;

/**
 * 属性文件  properties配置
 * @author 胡真山
 */
public class PropertiesExecutor implements Executor{

	protected static final String CLASS_PROPERTY_VAR=".class";
	
	protected String var;
	
	@Override
	public void setResultVar(String var) {
		this.var=var;
	}

	@Override
	public Object execute(String script, Object beanParams) {
		Template template=new SimpleStringTemplate(script);
		String target=template.processToString(beanParams);
		Properties p=new Properties();
		try {
			p.loadAndClose(new StringReader(target));
		} catch (IOException e) {
			throw new SmileRunException("解析内容："+script+"出错:param"+beanParams,e);
		}
		String classKey=var+CLASS_PROPERTY_VAR;
		String clazz=p.getProperty(classKey);
		Class resultClass=null;
		if(StringUtils.notEmpty(clazz)){
			resultClass=p.getValue(classKey, Class.class);
			p.remove(classKey);
		}
		Object resultObj=null;
		if(resultClass!=null){
			try {
				resultObj=resultClass.newInstance();
			} catch (Exception e) {
				throw new SmileRunException("初始化对象:"+resultClass+"失败",e);
			}
		}else{
			resultObj=new HashMap();
		}
		PropertyHandler handler=PropertyHandlers.getHanlder(resultClass);
		for(Map.Entry<Object, Object> entry:p.entrySet()){
			String property=((String)entry.getKey()).substring(var.length()+1);
			try {
				handler.setExpFieldValue(resultObj, property, entry.getValue());
			} catch (BeanException e) {
				throw new SmileRunException("初始化属性失败:"+resultClass+",property "+property+" value "+entry.getValue(),e);
			}
		}
		return resultObj;
	}

}
