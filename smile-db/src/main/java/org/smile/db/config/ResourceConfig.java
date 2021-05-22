package org.smile.db.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.json.JSON;

/***
 *  jdbc 数据源配置
 * @author 胡真山
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "Resource")
public class ResourceConfig {
	/**
	 * 数据源类名
	 */
	@XmlAttribute
	private String type;
	/**
	 * 数据源名称
	 */
	@XmlAttribute
	private String name;
	/**初始化连接个数*/
	@XmlAttribute
	private int initialSize;
	/**
	 * 驱动
	 */
	@XmlAttribute
	private String driver;
	/**
	 *  数据连接的 URL 
	 */
	@XmlAttribute
	private String url;
	/**
	 * 数据库用户名
	 */
	@XmlAttribute
	private String username;
	/**
	 * 数据库密码
	 */
	@XmlAttribute
	private String password;
	/**
	 *  初始化连接数
	 */
	@XmlAttribute
	private int minActive = 0;
	/**
	 * 最大连接数
	 */
	@XmlAttribute
	private int maxActive = 10;
	/**
	 *  连接的最大空闲时间
	 */
	@XmlAttribute
	private long timeOutValue = 600000;
	/**
	 * 等待时间
	 */
	@XmlAttribute
	private int maxWaitTime = 3000;
	/**获取连接时验证语句*/
	@XmlAttribute
	private String validationQuery;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getTimeOutValue() {
		return timeOutValue;
	}

	public void setTimeOutValue(long timeOutValue) {
		this.timeOutValue = timeOutValue;
	}

	public int getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/***
	 * 设置驱动程序类名
	 * 为了兼容apache 数据源配置
	 * @param driverClassName
	 */
	public void setDriverClassName(String driverClassName) {
		this.driver = driverClassName;
	}

	/***
	 * 获取驱动程序类名
	 * 为了兼容apache 数据源配置
	 * @param driverClassName
	 */
	public String getDriverClassName() {
		return this.driver;
	}

	/**
	 * 在抛出异常之前，池等待连接被回收的最长时间（当没有可用连接时）
	 * 为了兼容apache 数据源配置
	 * @return
	 */
	public int getMaxWait() {
		return this.maxWaitTime;
	}

	/***
	 * 池里不会被释放的最多空闲连接数量
	 * 为了兼容apache 数据源配置
	 * @return
	 */
	public int getMaxIdle() {
		return maxActive;
	}
	/**
	 * 最小空闲
	 * @return
	 */
	public int getMinIdle() {
		return minActive;
	}

	public int getMinActive() {
		return minActive;
	}

	public void setMinActive(int minActive) {
		this.minActive = minActive;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
