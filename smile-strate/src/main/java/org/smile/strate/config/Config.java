package org.smile.strate.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.config.BeansConfig;
import org.smile.config.IncludeConfig;

/**
 * strate文件配置根标签
 * 
 * @author 胡真山
 * @Date 2016年1月18日
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "strate")
public class Config {
	/**
	 * 对象工厂配置
	 */
	@XmlElementRef
	private BeansConfig beans;
	/** 配置注解扫描action路径 */
	@XmlElement(name = "action-scanner")
	private String actionScanner;
	@XmlElementRef
	private ActionUrlParserConfig actionUrlParser;
	@XmlElementRef
	private List<ConstantConfig> constant;
	@XmlElementRef
	private List<IncludeConfig> include;
	@XmlElementRef(name = "package")
	private List<PackageConfig> packages;
	@XmlElementRef(name = "global-results")
	private GlobalResultConfig globaResult;
	@XmlElementRef(name = "global-exception-mappings")
	private GlobaExceptionMappingConfig globaExceptionMapping;
	@XmlElementRef
	private InterceptorsConfig interceptors;
	@XmlElementRef
	private ExecutorConfig executor;
	/** 配置文件名称 */
	private String configFileName;

	public List<IncludeConfig> getInclude() {
		return include;
	}

	public GlobalResultConfig getGlobaResult() {
		return globaResult;
	}

	public GlobaExceptionMappingConfig getGlobaExceptionMapping() {
		return globaExceptionMapping;
	}

	public List<PackageConfig> getPackages() {
		return packages;
	}

	public List<ConstantConfig> getConstant() {
		return constant;
	}

	public InterceptorsConfig getInterceptors() {
		return interceptors;
	}

	public String getActionScanner() {
		return actionScanner;
	}

	public ExecutorConfig getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorConfig executor) {
		this.executor = executor;
	}

	public String getFileName() {
		return configFileName;
	}

	public void setFileName(String configFile) {
		this.configFileName = configFile;
	}

	public ActionUrlParserConfig getActionUrlParser() {
		return actionUrlParser;
	}

	public BeansConfig getBeans() {
		return beans;
	}

	public String getConfigFileName() {
		return configFileName;
	}
	
	

}
