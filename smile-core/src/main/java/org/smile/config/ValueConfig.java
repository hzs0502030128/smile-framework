package org.smile.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.util.StringUtils;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "value")
public class ValueConfig extends ValueConfigSupport implements RefSupport{
	@XmlAttribute
	private String ref;
	
	@XmlElementRef(name="bean")
	protected BeanConfig beanConfig;
	/**
	 * 获取参考配置
	 * @return
	 */
	@Override
	public String getRef(){
		if(StringUtils.notEmpty(ref)){
			return ref;
		}else if(beanConfig!=null){
			return beanConfig.getRef();
		}
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public BeanConfig getBeanConfig() {
		return beanConfig;
	}

	public void setBeanConfig(BeanConfig beanConfig) {
		this.beanConfig = beanConfig;
	}
	
}
