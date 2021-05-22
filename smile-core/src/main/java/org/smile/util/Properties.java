package org.smile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.Converter;
import org.smile.beans.handler.MapPropertyHandler;
import org.smile.collection.CollectionUtils;
import org.smile.collection.DataGetter;
import org.smile.collection.MapUtils;
import org.smile.collection.UnmodifiableList;
import org.smile.commons.SmileRunException;
import org.smile.config.Config;
import org.smile.config.XmlProperties;
import org.smile.config.YmlProperties;
import org.smile.io.FileNameUtils;
import org.smile.io.IOUtils;
import org.smile.io.ResourceUtils;
import org.smile.json.JSONArray;
import org.smile.json.JSONObject;
import org.smile.json.parser.JSONParseException;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.FieldUtils;
import org.smile.template.SimpleStringTemplate;

/**
 * properties文件 继承自 {@link java.util.Properties} 添加了一此快捷的类型转换方法
 * 
 * @author 胡真山 2015年11月25日
 */
public class Properties extends java.util.Properties implements Config, DataGetter<String> {

	protected BeanProperties beanProperties = new BeanProperties(false);
	/**
	 * 用于对层级的key获取值
	 */
	protected MapPropertyHandler propertyHandler = new MapPropertyHandler(beanProperties);
	/**类型转换器*/
	protected Converter converter = BasicConverter.getInstance();
	
	private static String YAML="yaml";
	private static String YML="yml";
	private static String XML="xml";
	private static String TXT="txt";
	private static String PROPERTIES="properties";
	
	private static final List<String> SUPPORT_EXTENSION=new UnmodifiableList<String>(CollectionUtils.arrayList(PROPERTIES,XML,YML,YAML,TXT));
	/**
	 * 加载classloader 文件 关闭流
	 * 
	 * @param name
	 * @throws IOException
	 */
	public void loadClassPathFile(String name) throws IOException {
		loadClassPathFile(name, true);
	}

	/**
	 * 加载classloader 文件 关闭流
	 * 
	 * @param name
	 * @throws IOException
	 */
	public void loadClassPathFile(String name, Object initParam) throws IOException {
		loadClassPathFile(name, initParam, true);
	}

	/**
	 * @param name
	 *            文件名
	 * @param notExistsError
	 *            不存在是否抛出异常
	 * @throws IOException
	 */
	public void loadClassPathFile(String name, boolean notExistsError) throws IOException {
		InputStream is =ResourceUtils.getResourceAsStream(name);
		if (is == null) {
			if (notExistsError) {
				throw new NullPointerException("can find a file in classpath named " + name);
			}
		} else {
			load(is);
		}
	}

	/**
	 * @param name
	 *            文件名
	 * @param notExistsError
	 *            不存在是否抛出异常
	 * @throws IOException
	 */
	public void loadClassPathFile(String name, Object initParam, boolean notExistsError) throws IOException {
		InputStream is = ResourceUtils.getResourceAsStream(name);
		if (is == null) {
			if (notExistsError) {
				throw new NullPointerException("can find a file in classpath named " + name);
			}
		} else {
			load(is, initParam);
		}
	}

	/**
	 * 加载一个文件后关闭流
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void load(File file) throws IOException {
		load(file, null);
	}

	/**
	 * 加载后并关闭reader
	 * 
	 * @param reader
	 * @throws IOException
	 */
	public void loadAndClose(Reader reader) throws IOException {
		try {
			load(reader);
		} finally {
			IOUtils.close(reader);
		}
	}

	/**
	 * 获取配置的值
	 * 
	 * @param key
	 *            配置文件中的键
	 * @param type
	 *            返回值的数据类型
	 * @return
	 * @throws ConvertException
	 *             在往目标类型转换的时候可能会出错异常
	 */
	public <T> T getValue(String key, Class<T> type) {
		Object value = getValue(key);
		try {
			return converter.convert(type, value);
		} catch (ConvertException e) {
			throw new SmileRunException(e);
		}
	}

	/**
	 * 以Integer类型返回
	 * 
	 * @param key
	 * @return
	 * @throws ConvertException
	 */
	public Integer getInteger(String key) {
		return getValue(key, Integer.class);
	}

	/**
	 * 以Integer类型返回
	 * 
	 * @param key
	 * @param deaultValue
	 *            为空时默认值
	 * @return
	 * @throws ConvertException
	 */
	public Integer getInteger(String key, Integer deaultValue) {
		return getValue(key, Integer.class, deaultValue);
	}

	/**
	 * 获取配置的值
	 * 
	 * @param key
	 *            配置文件中的键
	 * @param type
	 *            返回值的数据类型
	 * @param defalT
	 *            没有配置时默认的返回值
	 * @return
	 * @throws ConvertException
	 *             在往目标类型转换的时候可能会出错异常
	 */
	public <T> T getValue(String key, Class<T> type, T defalT) {
		Object value = getValue(key);
		if (value == null) {
			return defalT;
		}
		try {
			return converter.convert(type, value);
		} catch (ConvertException e) {
			throw new SmileRunException(e);
		}
	}

	/**
	 * 以boolean 类型为返回值
	 * 
	 * @param key
	 *            配置文件key
	 * @return
	 * @throws ConvertException
	 */
	@Override
	public Boolean getBoolean(String key) {
		return getValue(key, Boolean.class);
	}

	/**
	 * 以Boolean类型返回
	 * 
	 * @param key
	 *            配置文件key
	 * @param defaultValue
	 *            默认值
	 * @return boolean类型的值
	 * @throws ConvertException
	 *             当配置文件中的值无未法向boolean型转换的时候
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		return getValue(key, Boolean.class, defaultValue);
	}

	/**
	 * 转换成一个对象
	 * 
	 * @param type
	 * @return
	 * @throws BeanException
	 */
	public <T> T convertTo(Class<T> type) throws BeanException {
		T object = ClassTypeUtils.newInstance(type);
		for (Map.Entry<Object, Object> entry : entrySet()) {
			beanProperties.setExpFieldValue(object, keyToFieldName(entry), entry.getValue());
		}
		return object;
	}

	/**
	 * 把内容设置到一个类的静态字段上
	 * 
	 * @param type
	 * @throws ConvertException
	 */
	public void pushToStaticExpFields(Class type) throws ConvertException {
		for (Map.Entry<Object, Object> entry : entrySet()) {
			FieldUtils.setStaticExpFieldValue(type,keyToFieldName(entry), entry.getValue());
		}
	}

	/**
	 * 把内容设置到一个类的静态字段上
	 * 
	 * @param type
	 * @throws ConvertException
	 */
	public void pushToStaticNoCaseFields(Class type) throws ConvertException {
		Map<String, Field> fieldMap = FieldUtils.getNoCaseNameStaticFields(type);
		for (Map.Entry<Object, Object> entry : entrySet()) {
			Field f = fieldMap.get(keyToFieldName(entry));
			if (f != null) {
				FieldUtils.setStaticFieldValue(f, entry.getValue());
			}
		}
	}
	/**
	 * 往對象赋值时 对key进行转换
	 * @param entry
	 * @return
	 */
	private String keyToFieldName(Map.Entry entry) {
		return String.valueOf(entry.getKey());
	}

	/**
	 * 属性转换到对象中
	 * 
	 * @param config
	 * @throws BeanException
	 */
	public void convertTo(Object config) throws BeanException {
		for (Map.Entry<Object, Object> entry : entrySet()) {
			beanProperties.setExpFieldValue(config,keyToFieldName(entry), entry.getValue());
		}
	}

	public void load(InputStream is) throws IOException {
		load(is, null);
	}

	@Override
	public void load(File file, Object initParam) throws IOException {
		load(new FileInputStream(file), initParam);
	}

	@Override
	public void load(InputStream is, Object initParam) throws IOException {
		load(is, initParam, true);
	}

	public void load(InputStream is, Object initParam, boolean closeIs) throws IOException {
		try {
			super.load(is);
			if (initParam != null) {
				replaceParams(initParam);
			}
		} finally {
			if (closeIs) {
				IOUtils.close(is);
			}
		}
	}

	protected void replaceParams(Object params) {
		for (Map.Entry<Object, Object> entry : entrySet()) {
			String text = String.valueOf(entry.getValue());
			SimpleStringTemplate template = new SimpleStringTemplate(text);
			if (template.hasExpress()) {
				put(entry.getKey(), template.processToString(params));
			}
		}
	}

	public void load(Reader reader, Object initParam) throws IOException {
		try {
			super.load(reader);
			if (initParam != null) {
				replaceParams(initParam);
			}
		} finally {
			IOUtils.close(reader);
		}
	}

	@Override
	public <T> T getValue(String key) {
		try {
			return (T) propertyHandler.getExpFieldValue(this, key);
		} catch (BeanException e) {
			throw new SmileRunException("get value error - " + key, e);
		}
	}

	@Override
	public Collection getValues() {
		return values();
	}

	@Override
	public Set getKeys() {
		return keySet();
	}

	/**
	 * 添加别一个资源
	 * 
	 * @param properties
	 * @param replace
	 *            如是存在是否替换
	 */
	public void putAll(java.util.Properties properties, boolean replace) {
		if (replace) {
			putAll(properties);
		} else {
			for (Map.Entry<Object, Object> e : properties.entrySet()) {
				if (!containsKey(e.getKey())) {
					put(e.getKey(), e.getValue());
				}
			}
		}
	}

	public void setLocations(String locations) {
		try {
			InputStream is = ResourceUtils.getInputStreamForPath(locations);
			load(is, System.getProperties());
		} catch (Exception e) {
			throw new SmileRunException("load " + locations + " case a error ", e);
		}
	}

	@Override
	public Long getLong(String key) {
		return getValue(key, Long.class);
	}

	@Override
	public Float getFloat(String key) {
		return getValue(key, Float.class);
	}

	@Override
	public Double getDouble(String key) {
		return getValue(key, Double.class);
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		return getValue(key, BigDecimal.class);
	}

	@Override
	public String getString(String key) {
		return getValue(key, String.class);
	}

	@Override
	public Date getDate(String key) {
		return getValue(key, Date.class);
	}

	@Override
	public Integer getInt(String key) {
		return getValue(key, Integer.class);
	}

	@Override
	public Byte getByte(String key) {
		return getValue(key, Byte.class);
	}

	@Override
	public Short getShort(String key) {
		return getValue(key, Short.class);
	}

	@Override
	public <T> T get(String key, Class<T> resultClass) {
		return getValue(key, resultClass);
	}

	@Override
	public JSONObject getJSONObject(String key) {
		Object value = getValue(key);
		if (value instanceof JSONObject) {
			return (JSONObject) value;
		} else if (value instanceof String) {
			try {
				return new JSONObject((String) value);
			} catch (JSONParseException e) {
				throw new SmileRunException(e);
			}
		}
		throw new SmileRunException("not a json objct string");
	}

	@Override
	public JSONArray getJSONArray(String key) {
		return MapUtils.getJSONArray(this, key);
	}
	/**
	 * 从配置文件实例化一个属性配置对象
	 * @param fileName
	 * @return
	 */
	public static Properties instanceFromClassPath(String fileName) {
		String extension=FileNameUtils.getExtension(fileName);
		InputStream is=ResourceUtils.loadFromClassPath(fileName);
		if(is==null) {
			throw new SmileRunException(fileName+" not exists ");
		}
		return instanceProperties(extension, is);
	}
	/**
	 * 实现化配置文件 可根据扩展名做不同类型实例化
	 * @param extension
	 * @param is
	 * @return
	 */
	public static Properties instanceProperties(String extension,InputStream is){
		Properties props=null;
		if(XML.equals(extension)) {
			props=new XmlProperties();
		}else if(YML.equals(extension)||YAML.equals(extension)) {
			props=new YmlProperties();
		}else {
			props=new Properties();
		}
		try {
			props.load(is,System.getProperties());
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
		return props;
	}
	/**
	 * 从classpath实例化配置文件
	 * @param name
	 * @param extensions
	 * @return
	 */
	public static Properties instanceFromClassPath(String name,Collection<String> extensions) {
		Map.Entry<String, InputStream> entry=ResourceUtils.getResourceAsStream(name, extensions);
		if(entry!=null) {
			return instanceProperties(entry.getKey(),entry.getValue());
		}
		return null;
	}
	/**构建一个配置文件
	 * Properties p=Properties.build("config",new Properties());
	 * @param name 不包含扩展名
	 * @param notExistsDefault
	 * @return
	 */
	public static Properties build(String name,Properties notExistsDefault) {
		Properties props=instanceFromClassPath(name,SUPPORT_EXTENSION);
		if(props==null) {
			return notExistsDefault;
		}
		return props;
	}
	/**
	 * 	创建一个配置文件
	 * Properties p=Properties.build("config");
	 * @param name 只是文件名 在 classpath下 不包含扩展名
	 * @return
	 */
	public static Properties build(String name) {
		return build(name,new Properties());
	}
}
