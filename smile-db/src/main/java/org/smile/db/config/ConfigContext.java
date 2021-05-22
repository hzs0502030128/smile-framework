package org.smile.db.config;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.smile.collection.CollectionUtils;
import org.smile.db.DbConstans;
import org.smile.io.IOUtils;
import org.smile.log.LoggerHandler;
import org.smile.template.SimpleStringTemplate;
import org.smile.template.StringTemplate;
import org.smile.util.XmlUtils;

/**
 * 数据源配置文件
 * 
 * @author 胡真山
 */
public class ConfigContext implements LoggerHandler{

	private static ConfigContext context = null;

	/** 配置的jdbc连接信息 */
	private Map<String, JdbcConfig> jdbcConfigs = new HashMap<String, JdbcConfig>();
	/**
	 * datasource配置信息
	 */
	private Map<String, DataSourceConfig> datasourceConfigs = new HashMap<String, DataSourceConfig>();
	/**
	 * 数据源资源配置信息
	 */
	private Map<String, ResourceConfig> resourceConfigs = new HashMap<String, ResourceConfig>();

	public static ConfigContext getInstance() {
		if (context == null) {
			context = new ConfigContext();
		}
		return context;
	}

	private ConfigContext() {
		init();
	}
	/**
	 * 初始化配置文件
	 */
	protected void init() {
		InputStream is = ConfigContext.class.getClassLoader().getResourceAsStream(DbConstans.CONFIG_FILE);
		if (is == null) {
			logger.info(DbConstans.CONFIG_FILE + "文件不存在,没有使用simle对数据源进行管理");
		} else {
			try {
				StringTemplate st=new SimpleStringTemplate(IOUtils.readString(is));
				DbConfig config = XmlUtils.parserXml(DbConfig.class, st.processToString(System.getProperties()));
				if (CollectionUtils.notEmpty(config.getJdbc())) {
					for (JdbcConfig jdbc : config.getJdbc()) {
						jdbcConfigs.put(jdbc.getName(), jdbc);
					}
				}
				if (CollectionUtils.notEmpty(config.getResource())) {
					for (ResourceConfig resource : config.getResource()) {
						resourceConfigs.put(resource.getName(), resource);
					}
				}
				if (CollectionUtils.notEmpty(config.getDataSource())) {
					for (DataSourceConfig datasource : config.getDataSource()) {
						datasourceConfigs.put(datasource.getName(), datasource);
					}
				}
			} catch (Exception e) {
				logger.error("加载配置文件出错," + DbConstans.CONFIG_FILE, e);
			}

		}
	}

	public JdbcConfig getJdbcConfig(String name) {
		return jdbcConfigs.get(name);
	}

	public DataSourceConfig getDataSourceConfig(String name) {
		return datasourceConfigs.get(name);
	}

	public ResourceConfig getResourceConfig(String name) {
		return resourceConfigs.get(name);
	}

	public Collection<ResourceConfig> getAllResourceConfig() {
		return resourceConfigs.values();
	}

	public Collection<DataSourceConfig> getAllDataSourceConfig() {
		return datasourceConfigs.values();
	}
}
