package org.smile.strate.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.config.RefSupport;
import org.smile.strate.FieldStrateExecutor;
import org.smile.strate.StrateExecutor;
import org.smile.strate.action.StrateInitException;
import org.smile.strate.handler.BeanHandler;
import org.smile.strate.handler.StrateBeanHandler;
import org.smile.util.StringUtils;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "executor")
public class ExecutorConfig implements RefSupport{
	@XmlAttribute(name="class")
	private String clazz;
	@XmlElement(name="action-bean-handler")
	private String beanHandler;
	@XmlAttribute
	private String ref;
	
	private StrateExecutor instance;
	
	/**实始化实例*/
	private StrateExecutor initInstance(){
		if(StringUtils.notEmpty(clazz)){
			try {
				instance=(StrateExecutor)Class.forName(clazz).newInstance();
			} catch (Exception e) {
				throw new StrateInitException("初始化 executor失败", e);
			}
		}else{
			instance=new FieldStrateExecutor();
		}
		
		if(StringUtils.notEmpty(beanHandler)){
			try {
				instance.setActionBeanHandler((BeanHandler) Class.forName(beanHandler).newInstance());
			} catch (Exception e) {
				throw new StrateInitException("初始化 beanHandler失败",e);
			}
		}else{
			instance.setActionBeanHandler(new StrateBeanHandler());
		}
		return instance;
	}
	
	public String getClazz() {
		return clazz;
	}
	
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	public synchronized StrateExecutor getInstance() {
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
