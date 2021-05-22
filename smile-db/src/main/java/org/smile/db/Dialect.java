package org.smile.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smile.beans.converter.BeanException;
import org.smile.commons.ServiceFinder;
import org.smile.commons.SmileRunException;
import org.smile.db.function.FunctionHandler;
import org.smile.db.function.FunctionHanlderSupport;
import org.smile.db.function.SqlFunction;
import org.smile.db.meta.SqlServerTableMetaInfo;
import org.smile.db.meta.TableMetaInfo;
import org.smile.db.sql.PageSqlEngin;
import org.smile.db.sql.SimplePageSqlEngin;
import org.smile.db.sql.page.DialectPage;
import org.smile.db.sql.page.InforminDialectPage;
import org.smile.db.sql.page.MySQLDialectPage;
import org.smile.db.sql.page.OracleDialectPage;
import org.smile.db.sql.page.PostgreSQLDialectPage;
import org.smile.db.sql.page.RowNumberDialectPage;
import org.smile.db.sql.page.SQL2000DialectPage;
import org.smile.db.sql.page.SQLServerDialectPage;
import org.smile.file.ClassPathFileScaner;
import org.smile.file.ClassPathFileScaner.BaseVisitor;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.Properties;
import org.smile.util.RegExp;
import org.smile.util.SysUtils;

/**
 * 方言常量 simle中已经实现了这几种数据库的分页查询
 * @author strive
 */
public enum Dialect {

	mysql, mariadb, sqlite, oracle, hsqldb, postgresql, sqlserver2000, sqlserver2005, sqlserver2008, db2, informix, derby;

	private static Map<Dialect, Class<? extends DialectPage>> dialectPageClass = new HashMap<Dialect, Class<? extends DialectPage>>();
	
	static final String CONFIG_DIR="META-INF/db/";
	/***
	 * 方言分页实现类
	 */
	private Class dialectPageClz;

	/**通用函数处理类*/
	private FunctionHandler functionHandler;
	
	private static volatile Class<PageSqlEngin> sqlEnginClass=null;
	
	static {
		registDefaultPage();
		try{
			registDefaultFunction();
			SysUtils.log("支持的osql函数有："+FunctionHanlderSupport.showFunctionSupport());
		}catch(Throwable e){
			SysUtils.log("不支持osql函数功能"+e);
		}
	}
	
	private PageSqlEngin sqlEngin;
	
	private Dialect(){
		try {
			this.sqlEngin=findImplClass().getConstructor(Dialect.class).newInstance(this);
		} catch (Exception e) {
			this.sqlEngin=new SimplePageSqlEngin(this);
		}
	}
	
	private Class<PageSqlEngin> findImplClass() throws ClassNotFoundException{
		if(sqlEnginClass==null){
			//分页引擎
			String pageSqlEnginImpl=ServiceFinder.findImpl(CONFIG_DIR, PageSqlEngin.class.getName(), SimplePageSqlEngin.class.getName());
			sqlEnginClass=(Class<PageSqlEngin>) Class.forName(pageSqlEnginImpl);
		}
		return sqlEnginClass;
	}

	public void setDialectPageClz(Class dialectPageClz) {
		this.dialectPageClz = dialectPageClz;
	}

	public void setFunctionHandler(FunctionHandler functionHandler) {
		this.functionHandler = functionHandler;
	}

	/**
	 * 处理函数
	 * @param f
	 */
	public void convert(SqlFunction f){
		if(this.functionHandler!=null){
			functionHandler.convertFuction(f);
		}
	}
	
	public FunctionHandler getFunctionHandler() {
		return functionHandler;
	}

	/**
	 * 处理函数
	 * @param f
	 */
	public void convert(List<SqlFunction> flist){
		if(this.functionHandler!=null){
			for(SqlFunction f:flist){
				functionHandler.convertFuction(f);
			}
		}
	}
	
	

	public static Dialect of(String dialect) {
		try {
			Dialect d = Dialect.valueOf(dialect);
			return d;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("分页dialect:[" + dialect + "]参数值错误，可选值为[" + tostring() + "]");
		}
	}

	public static String tostring() {
		StringBuffer dialects = new StringBuffer();
		for (Dialect d : Dialect.values()) {
			dialects.append(",").append(d);
		}
		return dialects.substring(1);
	}

	/**实例化一个分页方言类对象*/
	public static DialectPage newDialectPage(Dialect dialet, String sql) throws SQLException {
		DialectPage dialectPage;
		Class clazz = dialet.dialectPageClz;
		try {
			dialectPage = (DialectPage) dialet.dialectPageClz.getConstructor(String.class).newInstance(sql);
		} catch (NoSuchMethodException e) {
			throw new SQLException("请实现[DialectPage]接口自定义分页,构造方法(sql)", e);
		} catch (Exception e) {
			throw new SQLException("construct sql pager error " + clazz + " sql:" + sql, e);
		}

		return dialectPage;
	}

	/**
	 * 跟据方言来实例化出一个数据库表信息获取的工具对象
	 * @param tableName 要获取信息的表名
	 * @return 适合数据库的表信息获取工具
	 */
	public TableMetaInfo newTableMetaInfo(String tableName) {
		if (this == sqlserver2000 || this == sqlserver2005 || this == sqlserver2008) {
			return new SqlServerTableMetaInfo(tableName);
		} else {
			return new TableMetaInfo(tableName);
		}
	}

	/**
	 * @param sql 要查询的sql信息
	 * @return 转换后的分页sql信息
	 * @throws SQLException 没有为方言指定 {@link DialectPage}  的实现类
	 */
	public DialectPage getDialectPage(String sql){
		return this.sqlEngin.getDialectPage(sql);
	}

	/**注册方言分页  可以自定义的类 替换原有的类 */
	public static void register(String dialect, Class<? extends DialectPage> clazz) {
		dialectPageClass.put(of(dialect), clazz);
	}

	/**
	 * 默认的分页实现类
	 */
	private static void registDefaultPage(){
		mysql.setDialectPageClz(MySQLDialectPage.class);
		oracle.setDialectPageClz(OracleDialectPage.class);
		hsqldb.setDialectPageClz(PostgreSQLDialectPage.class);
		sqlserver2000.setDialectPageClz(SQL2000DialectPage.class);
		sqlserver2005.setDialectPageClz(SQLServerDialectPage.class);
		sqlserver2008.setDialectPageClz(SQLServerDialectPage.class);
		derby.setDialectPageClz(RowNumberDialectPage.class);
		db2.setDialectPageClz(RowNumberDialectPage.class);
		postgresql.setDialectPageClz(PostgreSQLDialectPage.class);
		mariadb.setDialectPageClz(MySQLDialectPage.class);
		sqlite.setDialectPageClz(MySQLDialectPage.class);
		informix.setDialectPageClz(InforminDialectPage.class);
	}
	
	/***
	 * 默认的函数处理类
	 */
	private static void registDefaultFunction(){
		final RegExp CONFIG_NAME_EXP=new RegExp("db-function-handler.*");
		//扫描目录
		ClassPathFileScaner scaner = new ClassPathFileScaner(CONFIG_DIR,new BaseVisitor() {
			@Override
			public boolean visit(String fileName,InputStream is) throws IOException {
				Properties p=new Properties();
				p.load(is);
				for(Object key:p.keySet()){
					Dialect dialect=Dialect.valueOf((String)key);
					FunctionHandler handler;
					try {
						handler = ClassTypeUtils.newInstance(p.getProperty((String)key));
						dialect.setFunctionHandler(handler);
					} catch (BeanException e) {
						SysUtils.log("regist function handler error "+key,e);
					}
				}
				return false;
			}

			@Override
			public boolean accept(String fileName, String protocol) {
				return CONFIG_NAME_EXP.matches(fileName);
			}
		});
		try {
			scaner.scanning();
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}
}
