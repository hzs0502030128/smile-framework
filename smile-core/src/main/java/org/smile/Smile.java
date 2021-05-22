package org.smile;

import org.smile.annotation.AnnotationUtils;
import org.smile.beans.ConfigBeanCreater;
import org.smile.beans.converter.BeanException;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.commons.ann.Config;
import org.smile.config.BeansConfigProperties;
import org.smile.util.Properties;
import org.smile.util.StringUtils;
import org.smile.util.SysUtils;
/**
 * smile 的一些配置信息
 * @author 胡真山
 * 
 * smile.properties 配置文件可配置
 * validate.message.file 可配置验证框架消息i18n文件名
 * db_file_name  数据库文件  [可选]  默认smile_db.xml
 * encode 默认字符编码  [可选]   默认 utf-8
 * db_dialect 默认数据库方言 [可选]  默认 mysql
 * coder.excelColumns excel与数据库对应配置信息获取表   [使用时必选]  例如   coder.excelColumns=0,1,3,4
 * pager.pageSize [可选] 分页参数  配置分页条数
 * export.templateDir [可选] xls导出模板目录
 * i18n.defaultLocal  [可选]  用于国际化默认的方言    例如   en 或者 en_US 
 * logger.LoggerFactory [可选] 日志工厂配置
 * reflect.paramNameReader [可选] 方法参数名读取器 不设置时怎么匹配
 * json.adpater [可选] json适配器   可自动匹配
 */
public class Smile{

	private static final String SYSTEM_PROFILES_ACTIVE_KEY ="smile.profiles.active";
	/**用于保存 HotInstrumentation 信息在System properties  中 做为key*/
	public static final String HOT_INSTRUMENTATION_KEY="smile-hot-instrumentation";
	/**换行符*/
	public static final String LINE_SEPARATOR=SysUtils.getLineSeparator();
	/**cglib生成的类的名称标识*/
	public static String CGLIB_CLASS_FLAG="$EnhancerByCGLIB$";
	/**smile的配置文件名称 */
	private static String SMILE_CONFIG_FILE_NAME="smile";
	/**数据库配置文件文件名  不配置时默认 smile_db.xml*/
	public static String DB_FILE_NAME="smile_db.xml";
	/**默认数据方言   不配置时默认mysql*/
	public static String DB_DIALECT="mysql";
	/**字条编码  默认 UTF-8*/
	public static String ENCODE="UTF-8";
	/**IO 默认的缓冲数组长度*/
	public static int IO_READ_BUFF_SIZE=1024;
	/** 默认的国际化方言配置文件key*/
	public static String I18N_DEFAULT_LOCAL_KEY="i18n.defaultLocal";
	/*** 默认的导出模板目标配置key*/
	public static String EXPORT_TEMPLATE_DIR_KEY="export.templateDir";
	/** * 默认的分页条数配置key */
	public static String PAGER_PAGE_SIZE_KEY="pager.pageSize";
	/** * 默认的国际化资源文件名称 */
	public static String MESSAGE_RESOURCES_NAME="MessageResources";
	/** * 日志工厂类配置键 */
	public static String LOG_FACTORY_CONFIG_KEY="logger.loggerFactory";
	/** * 日志配置文件名称名 */
	public static String LOG_CONFIG_FILE="logger";
	/** * JSON适配器配置键*/
	public static String JSON_ADPATER_CONFIG_KEY="json.adpater";
	/**用于配置模板类型默认值的键*/
	public static String TEMPLATE_TYPE_DEFAULT_KEY="template.defaultType";
	/*** 方法参数名读取器 */
	public static String PARAM_NAME_READER_KEY="reflect.paramNameReader";
	/**配置http 超时时间*/
	public static String HTTP_TIMEOUT_KEY="http.timeout";
	/**xml解析时文本结点key*/
	public static String XML_PARSER_TEXT_NODE_KEY="value";
	/**用于配置星期的第一天是星期几key*/
	public static String FIRST_DAY_OF_WEEK="calendar.firstDayOfWeek";
	/**一年的第一周最少需要天数*/
	public static String MINIMAL_DAYS_INFIRST_WEEK="calendar.minimalDaysInFirstWeek";
	/**配置实现的工厂实现类*/
	public static String CONFIG_BEAN_FACTORY="config.beanFactory";
	/**配置程序入口*/
	public static String CONFIG_APPLICATION="config.application";
	/** * smile的配置文件信息 */
	public static Properties config=new Properties();
	/**属性配置文件*/
	private static BeansConfigProperties configBeans=new BeansConfigProperties();
	
	static {
		try {
			config=Properties.build(SMILE_CONFIG_FILE_NAME,config);
			String activeName= System.getProperty(SYSTEM_PROFILES_ACTIVE_KEY);
			if(StringUtils.notEmpty(activeName)){
				Properties active=Properties.build(SMILE_CONFIG_FILE_NAME+ Strings.DASH+activeName);
				config.putAll(active,true);
			}
			config.pushToStaticNoCaseFields(Smile.class);
		} catch (Throwable e) {
			SysUtils.println("load smile.properties error ",e);
		}
		SysUtils.println("load smile.properties end,values:"+config);
	}
	/**
	 * 	获取使用@Config注解了的配置对象
	 * @param <T>
	 * @param configClass 配置对象的类型
	 * @return 配置对象的实例
	 */
	public static <T> T getConfig(Class<T> configClass) {
		synchronized (configClass) {
			try {
				T bean = configBeans.getBean(configClass);
				if(bean!=null) {
					return bean;
				}else {
					Config configAnn=AnnotationUtils.getAnnotation(configClass, Config.class);
					if(configAnn==null) {
						throw new SmileRunException(configClass+" not has a @"+Config.class.getName());
					}
					ConfigBeanCreater beanCreator=new ConfigBeanCreater(configAnn.value(),configClass);
					configBeans.registProducer(beanCreator.getBeanId(),beanCreator);
					return configBeans.getBean(configClass);
				}
			} catch (BeanException e) {
				throw new SmileRunException(e);
			}
		}
	}
	
}
