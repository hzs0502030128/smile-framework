package org.smile.db.jdbc;
/**
 * 一个简单的封装
 * @author 胡真山
 *
 * @param <T>
 */
public class SimpleJdbcMapper<T> implements JdbcMapper<T>{
	/**表配置信息*/
	protected TableInfoCfg cfg;
	/**返回对象封装*/
	protected Class<T> mapperClass;
	
	public SimpleJdbcMapper(Class<T> clazz,TableInfoCfg cfg){
		this.mapperClass=clazz;
		this.cfg=cfg;
	}
	
	@Override
	public Class<T> mapperClass() {
		return mapperClass;
	}

	@Override
	public TableInfoCfg tableConfig() {
		return cfg;
	}
	
	public JdbcMapRecord buildMap(){
		return cfg.buildRecord();
	}
	
}
