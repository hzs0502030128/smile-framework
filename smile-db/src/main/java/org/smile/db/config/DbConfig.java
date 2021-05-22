package org.smile.db.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "Config")
public class DbConfig {
	private List<JdbcConfig> Jdbc;
	private List<ResourceConfig> Resource;
	private List<DataSourceConfig> DataSource;
	public List<JdbcConfig> getJdbc() {
		return Jdbc;
	}
	public List<ResourceConfig> getResource() {
		return Resource;
	}
	public List<DataSourceConfig> getDataSource() {
		return DataSource;
	}
	
}
