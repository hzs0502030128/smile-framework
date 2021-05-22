package org.smile.db.pool;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

import org.smile.db.config.ConfigContext;
import org.smile.db.config.ResourceConfig;
import org.smile.db.pool.init.DataSourceInitUtil;
import org.smile.log.LoggerHandler;

/**
 * 连接池操作类
 * @author strive
 *
 */
public class DataSourceContext implements LoggerHandler{
	
	 // 该哈希表用来保存数据源名和连接池对象的关系表
	 private static Map<String,DataSource> dataSourceMap = new ConcurrentHashMap<String,DataSource>(2,0.75F); 
	 
	 //从配置文件初始化连接池
	 static{
	   init();
	 }
	 
	 private static void init(){
			try {
				Collection<ResourceConfig> configs=ConfigContext.getInstance().getAllResourceConfig();
				for(ResourceConfig config:configs){
					try{
						dataSourceMap.put(config.getName(),bind(config.getName(), config));
					}catch(Throwable e){
						unbind(config.getName());
						logger.error("初始化数据源["+config.getName()+"]失败 "+e.getMessage(),e);
					}
				}
				logger.info("数据源连接池容器初始化完成……");
				logger.info(dataSourceMap);
			} catch (Exception e) {
				logger.error("连接池初始化失败：",e);
			}
	 }
	 /** 
	 * 从连接池工厂中获取指定名称对应的连接池对象
	 * @param dataSource 	连接池对象对应的名称
	 * @return DataSource 	返回名称对应的连接池对象
	 * @throws NameNotFoundException 	无法找到指定的连接池
	 */ 
	 public static DataSource lookup(String dataSourceName) 
		 throws NameNotFoundException 
	 { 
		 Object  ds = dataSourceMap.get(dataSourceName); 
		 if(ds == null || !(ds instanceof DataSource)){
			 throw new NameNotFoundException("没有绑定的数据源:"+dataSourceName); 
		 }
		 return (DataSource)ds; 
	 } 
	 /** 
	 * 将指定的名字和数据库连接配置绑定在一起并初始化数据库连接池
	 * @param name 		对应连接池的名称
	 * @param param 	连接池的配置参数，具体请见类 ConnectionParam 
	 * @return DataSource 	如果绑定成功后返回连接池对象
	 * @throws NameAlreadyBoundException 	一定名字 name 已经绑定则抛出该异常
	 * @throws ClassNotFoundException 		无法找到连接池的配置中的驱动程序类
	 * @throws IllegalAccessException 		连接池配置中的驱动程序类有误
	 * @throws InstantiationException 		无法实例化驱动程序类
	 * @throws SQLException 				无法正常连接指定的数据库
	 */ 
	 public static DataSource bind(String dataSourceName, ResourceConfig config) 
		 throws NameAlreadyBoundException,ClassNotFoundException, 
				 IllegalAccessException,InstantiationException,SQLException 
	 { 
		 DataSource source=null;
		 try{ 
			 source=lookup(dataSourceName); 
		 }catch(NameNotFoundException e){ 
			source = DataSourceInitUtil.initDataSource(config);
			dataSourceMap.put(dataSourceName, source); 
			if(logger.isDebugEnabled()){
				logger.debug("new datasource ");
				logger.debug(source);
				logger.debug(dataSourceMap);
			}
		 } 
		 return source; 
	 } 
	 
	 /** 
	 * 重新绑定数据库连接池
	 * @param name 		对应连接池的名称
	 * @param param 	连接池的配置参数，具体请见类 ConnectionParam 
	 * @return DataSource 	如果绑定成功后返回连接池对象
	 * @throws NameAlreadyBoundException 	一定名字 name 已经绑定则抛出该异常
	 * @throws ClassNotFoundException 		无法找到连接池的配置中的驱动程序类
	 * @throws IllegalAccessException 		连接池配置中的驱动程序类有误
	 * @throws InstantiationException 		无法实例化驱动程序类
	 * @throws SQLException 				无法正常连接指定的数据库
	 */ 
	 public static DataSource rebind(String name, ResourceConfig param) 
		 throws NameAlreadyBoundException,ClassNotFoundException, 
				 IllegalAccessException,InstantiationException,SQLException 
	 { 
		 try{ 
			 unbind(name); 
		 }catch(Exception e){
			 logger.error("重新绑定数据源出错",e);
		 } 
		 return bind(name, param); 
	 }
	 /**
	  * 重新绑定所有配置文件中的数据源
	  * @throws NameAlreadyBoundException
	  * @throws ClassNotFoundException
	  * @throws IllegalAccessException
	  * @throws InstantiationException
	  * @throws SQLException
	  */
	 public static void rebindAll() throws NameAlreadyBoundException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException{
		 for(ResourceConfig config:ConfigContext.getInstance().getAllResourceConfig()){
			 rebind(config.getName(),config);
		 }
	 }
	 /** 
	 * 删除一个数据库连接池对象
	 * @param name 
	 * @throws NameNotFoundException 
	 */ 
	 public static void unbind(String name) throws NameNotFoundException 
	 { 
		 DataSource dataSource=null;
		 try{
			 dataSource= lookup(name); 
		 }catch(Exception e){
			 logger.info("被从新绑定的数据源不存在，将会重新绑定不影响重新绑定");
		 }
		 if(dataSource!=null&&dataSource instanceof BasicDataSource){ 
			 BasicDataSource bds = (BasicDataSource)dataSource; 
			 try{ 
				 bds.close(); 
			 }catch(Exception e){ 
				 logger.error("关闭数据源出错",e);
			 }
		 } 
		 dataSourceMap.remove(name); 
	 } 
	 /** 
	 * 删除所有数据库连接池对象
	 * @param name 
	 * @throws NameNotFoundException 
	 */ 
	 public static void unbindAll() throws NameNotFoundException 
	 { 
		 Set<String> keySet=dataSourceMap.keySet();
		 for(Iterator<String> iter=keySet.iterator();iter.hasNext();){
			 DataSource dataSource = lookup((String)iter.next()); 
			 if(dataSource instanceof BasicDataSource){ 
				 BasicDataSource bds = (BasicDataSource)dataSource; 
				 try{ 
					 bds.close(); 
				 }catch(Exception e){ 
					 logger.error("关闭数据源出错",e);
				 }finally{ 
					 bds = null; 
				 } 
			 } 
		 }
		 dataSourceMap.clear(); 
	 } 
}
