package org.smile.strate.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.smile.config.RefSupport;
import org.smile.strate.StrateExecutor;
import org.smile.strate.action.StrateInitException;
import org.smile.strate.dispatch.ActionURIParser;
import org.smile.strate.dispatch.ExtensionActionURIParser;
import org.smile.util.StringUtils;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "action-url-parser")
public class ActionUrlParserConfig implements RefSupport{
	@XmlAttribute(name="class")
	private String clazz;
	@XmlAttribute
	private String ref;
	@XmlValue
	private String text;
	
	private ActionURIParser instance;
	
	/**实始化实例*/
	private ActionURIParser initInstance(){
		if(StringUtils.notEmpty(clazz)){
			try {
				instance=(ActionURIParser)Class.forName(getClazz()).newInstance();
			} catch (Exception e) {
				throw new StrateInitException("初始化 executor失败", e);
			}
		}else{
			instance=new ExtensionActionURIParser();
		}
		
		return instance;
	}
	
	public String getClazz() {
		return clazz==null?text:clazz;
	}
	
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	public synchronized ActionURIParser getInstance() {
		if(this.instance==null) {
			return this.initInstance();
		}
		return instance;
	}

	@Override
	public String getRef() {
		return ref;
	}
	
}
