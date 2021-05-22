package org.smile.db.pool.init;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.smile.db.config.ResourceConfig;
import org.smile.db.pool.BasicDataSource;
import org.smile.util.StringUtils;

public class DataSourceInitUtil {
	/**
	 * 默认的数据源类型
	 */
	private static final String DEFAUT_DATESOURCE_TYPE=BasicDataSource.class.getName();
	
	private static Map<String,DataSourceInitHandler> handlers=new HashMap<String,DataSourceInitHandler>();
	
	static{
		handlers.put(DEFAUT_DATESOURCE_TYPE, new SmileSourceInitHandler());
		handlers.put("org.apache.commons.dbcp.BasicDataSource",new ApacheBasicSourceInitHandler());
		handlers.put("org.apache.tomcat.dbcp.dbcp.BasicDataSource",new ApacheBasicSourceInitHandler());
		handlers.put("org.smile.db.pool.Pool2DataSource",new Pool2DataSourceInitHandler());
	}
	
	public static <E extends DataSource> E initDataSource(ResourceConfig config) throws SQLException{
		String type=config.getType();
		if(StringUtils.isEmpty(type)){
			type=DEFAUT_DATESOURCE_TYPE;
		}
		DataSourceInitHandler handler=handlers.get(type);
		return (E)handler.init(config);
	}
}
