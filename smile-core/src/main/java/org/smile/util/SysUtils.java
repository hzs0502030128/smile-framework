package org.smile.util;

import java.io.File;
import java.util.Map;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.log.Logger.Level;
import org.smile.log.record.LogRecord;
import org.smile.log.record.RecordFormatter;
import org.smile.log.record.SimpleRecordFormatter;
/**
 * 系统工具类 一些jdk 与操作系统信息的方法
 * @author 胡真山
 * 2015年10月22日
 */
public class SysUtils {
	/**换行符*/
	public static final String LINE_SEPARATOR="line.separator";
	public static final String USER_DIR = "user.dir";
	public static final String USER_NAME = "user.name";
	public static final String USER_HOME = "user.home";
	public static final String JAVA_HOME = "java.home";
	public static final String TEMP_DIR = "java.io.tmpdir";
	public static final String OS_NAME = "os.name";
	public static final String OS_VERSION = "os.version";
	public static final String JAVA_VERSION = "java.version";
	public static final String JAVA_SPECIFICATION_VERSION = "java.specification.version";
	public static final String JAVA_VENDOR = "java.vendor";
	public static final String JAVA_CLASSPATH = "java.class.path";
	public static final String PATH_SEPARATOR = "path.separator";
	public static final String HTTP_PROXY_HOST = "http.proxyHost";
	public static final String HTTP_PROXY_PORT = "http.proxyPort";
	public static final String HTTP_PROXY_USER = "http.proxyUser";
	public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";
	public static final String FILE_ENCODING = "file.encoding";
	public static final String SUN_BOOT_CLASS_PATH = "sun.boot.class.path";
	public static final String CATALINA_HOME="catalina.home";
	public static final String CATALINA_BASE="catalina.base";
	public static final String COMPUTER_NAME="COMPUTERNAME";
	
	private final static RecordFormatter formatter=new SimpleRecordFormatter();
	
	
	/**
	 * Returns current working folder.
	 */
	public static String getUserDir() {
		return System.getProperty(USER_DIR);
	}

	/**
	 * Returns current user.
	 */
	public static String getUserName() {
		return System.getProperty(USER_NAME);
	}

	/**
	 * Returns user home folder.
	 */
	public static String getUserHome() {
		return System.getProperty(USER_HOME);
	}


	/**
	 * Returns JRE home.
	 */
	public static String getJavaJreHome() {
		return System.getProperty(JAVA_HOME);
	}

	/**
	 * Returns JAVA_HOME which is not equals to "java.home" property
	 * since it points to JAVA_HOME/jre folder.
	 */
	public static String getJavaHome() {
		String home = System.getProperty(JAVA_HOME);
		if (home == null) {
			return null;
		}
		int i = home.lastIndexOf('\\');
		int j = home.lastIndexOf('/');
		if (j > i) {
			i = j;
		}
		return home.substring(0, i);
	}

	/**
	 * Returns system temp dir.
	 */
	public static String getTempDir() {
		return System.getProperty(TEMP_DIR);
	}

	/**
	 * Returns OS name.
	 */
	public static String getOsName() {
		return System.getProperty(OS_NAME);
	}

	/**
	 * Returns OS version.
	 */
	public static String getOsVersion() {
		return System.getProperty(OS_VERSION);
	}

	/**
	 * java version
	 * @return java 版本信息
	 */
	public static String getJavaVersion() {
		return System.getProperty(JAVA_VERSION);
	}
	/**
	 * jdk 编号
	 * @return 1.6 1.7 
	 */
	public static String getJDK(){
		String jdk=getJavaVersion();
		return jdk.substring(0, 3);
	}

	/**
	 * Retrieves the version of the currently running JVM.
	 */
	public static String getJavaSpecificationVersion() {
		return System.getProperty(JAVA_SPECIFICATION_VERSION);
	}


	/**
	 * Returns Java vendor.
	 */
	public static String getJavaVendor() {
		return System.getProperty(JAVA_VENDOR);
	}


	/**
	 * Returns system class path.
	 */
	public static String getClassPath() {
		return System.getProperty(JAVA_CLASSPATH);
	}

	/**
	 * Returns path separator.
	 */
	public static String getPathSeparator() {
		return System.getProperty(PATH_SEPARATOR);
	}

	/**
	 * Returns file encoding.
	 */
	public static String getFileEncoding() {
		return System.getProperty(FILE_ENCODING);
	}

	/**
	 * Returns <code>true</code> if host is Windows.
	 */
	public static boolean isHostWindows() {
		return getOsName().toUpperCase().startsWith("WINDOWS");
	}

	/**
	 * Returns <code>true</code> if host is Linux.
	 */
	public static boolean isHostLinux() {
		return getOsName().toUpperCase().startsWith("LINUX");
	}

	/**
	 * Returns <code>true</code> if host is a general unix.
	 */
	public static boolean isHostUnix() {
		boolean unixVariant = isHostAix() | isHostLinux() | isHostMac() | isHostSolaris();

		return (!unixVariant && File.pathSeparator.equals(Strings.COLON));
	}

	/**
	 * Returns <code>true</code> if host is Mac.
	 */
	public static boolean isHostMac() {
		return getOsName().toUpperCase().startsWith("MAC OS X");
	}

	/**
	 * Returns <code>true</code> if host is Solaris.
	 */
	public static boolean isHostSolaris() {
		return getOsName().toUpperCase().startsWith("SUNOS");
	}

	/**
	 * Returns <code>true</code> if host is AIX.
	 */
	public static boolean isHostAix() {
		return getOsName().toUpperCase().equals("AIX");
	}

	/**
	 * Returns bootstrap class path.
	 */
	public static String getSunBoothClassPath() {
		return System.getProperty(SUN_BOOT_CLASS_PATH);
	}


	/**
	 * Sets HTTP proxy settings.
	 */
	public static void setHttpProxy(String host, String port, String username, String password) {
		System.getProperties().put(HTTP_PROXY_HOST, host);
		System.getProperties().put(HTTP_PROXY_PORT, port);
		System.getProperties().put(HTTP_PROXY_USER, username);
		System.getProperties().put(HTTP_PROXY_PASSWORD, password);
	}

	/**
	 * Sets HTTP proxy settings.
	 */
	public static void setHttpProxy(String host, String port) {
		System.getProperties().put(HTTP_PROXY_HOST, host);
		System.getProperties().put(HTTP_PROXY_PORT, port);
	}
	/** tomcat 目录
	 * @return
	 */
	public static String getTomcatHome(){
		return System.getProperty(CATALINA_HOME);
	}
	/**
	 * 参见System.out.prinltn()
	 * @param obj
	 */
	public static void println(Object obj){
		System.out.println(obj);
	}
	
	public static void print(Object obj){
		System.out.print(obj);
	}
	
	public static void println(String message,Throwable throwable){
		System.out.println(message);
		if(throwable!=null){
			throwable.printStackTrace();
		}
	}
	/**
	 * 记录日志
	 * @param message
	 */
	public static void log(Object message){
		LogRecord record=new LogRecord(SysUtils.class,"system",Level.INFO,message);
		println(formatter.format(record));
	}
	/**
	 * 记录日志
	 * @param message
	 */
	public static void log(Object message,Throwable e){
		LogRecord record=new LogRecord(SysUtils.class,"system",Level.INFO,message);
		println(formatter.format(record),e);
	}
	/**
	 * @return 换行符
	 */
	public static String getLineSeparator(){
		return System.getProperty(LINE_SEPARATOR);
	}
	/**
	 * 设置一个属性到系统属性中
	 * @param name
	 * @param value
	 */
	public static void setProperty(String name,String value){
		System.setProperty(name, value);
	}
	/**
	 * 添加多个属性
	 * @param properties
	 */
	public static void addProperties(Map<String,String> properties){
		System.getProperties().putAll(properties);
	}
	/**
	 * 获取 System 配置文件中的值   参考{@link System } getProperty 
	 * @param key
	 * @return
	 */
	public static String getString(String key){
		return System.getProperty(key);
	}
	/**
	 * 返回属性将转换成目标类型
	 * @param key 配置文件中的键
	 * @param type 返回类型
	 * @return 
	 */
	public static <T> T getProperty(String key,Class<T> type){
		String value=System.getProperty(key);
		try {
			return BasicConverter.getInstance().convert(type, value);
		} catch (ConvertException e) {
			throw new SmileRunException("get property from system error key :"+key,e);
		}
	}
	/**
	 * 打印出系统信息
	 */
	public static void printSysInfo(){
		printPropertiesInfo(System.getProperties());
	}
	/**
	 * 打印配置属性
	 */
	public static void printPropertiesInfo(java.util.Properties properties){
		for(Map.Entry<Object, Object> entry:properties.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}
	}
	/**
	 * 获取计算机名
	 * @return
	 */
	public String getComputerName(){
		return System.getenv(COMPUTER_NAME);
	}
	
	/**
	 * Checks the <code>os.name</code> system property to see if it starts with
	 * "windows".
	 *
	 * @return <code>true</code> if <code>os.name</code> starts with "windows",
	 *         else <code>false</code>.
	 */
	public static boolean isWindowsOS() {
		return System.getProperty(OS_NAME).toLowerCase().startsWith("windows");
	}
}
