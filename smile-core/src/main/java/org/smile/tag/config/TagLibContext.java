package org.smile.tag.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.expression.Engine.FunctionKey;
import org.smile.file.ClassPathFileScaner;
import org.smile.file.ClassPathFileScaner.BaseVisitor;
import org.smile.function.ConfigurableFunction;
import org.smile.function.Function;
import org.smile.io.IOUtils;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.MethodUtils;
import org.smile.tag.State;
import org.smile.tag.Tag;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;
import org.smile.util.SysUtils;
import org.smile.util.XmlUtils;

public class TagLibContext {

	public static final String DEFAULT_CORE = "tag-smile-core";

	public static final String DEFAULT_FUNCTIONS = "function-smile-core";
	/** 控制台命令配置文件 */
	protected static final RegExp CONFIG_NAME_EXP = new RegExp(".+\\.std\\.xml");
	/** 控制台命令配置文件目录 */
	protected static final String CONFIG_DIR = "META-INF/";
	/*** 所有的命令集合 */
	private Set<TagLibConfig> tagLibs = new TreeSet<TagLibConfig>();

	private Set<TagLibConfig> functionLibs = new TreeSet<TagLibConfig>();

	private static TagLibContext instance = new TagLibContext();

	private TagLibContext() {
		//扫描目录
		ClassPathFileScaner scanner = new ClassPathFileScaner(CONFIG_DIR,new BaseVisitor() {
			@Override
			public boolean visit(String fileName,InputStream is) throws IOException {
				try {
					loadConfigFile(is);
				}catch(Exception e){
					throw new IOException(fileName,e);
				}
				return false;
			}

			@Override
			public boolean accept(String fileName, String protocol) {
				return CONFIG_NAME_EXP.matches(fileName);
			}
		});
		try {
			scanner.scanning();
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}

	public static TagLibContext getInstance() {
		return instance;
	}

	/**
	 * 注册标签库
	 * 
	 * @param state
	 * @param prefix
	 *            前缀
	 * @param tagLibName
	 *            标签库名称
	 */
	public void registTags(Map<String, Class> libMap, String prefix, String tagLibName) {
		TagLibConfig config = getTagLibConfig(tagLibName);
		for (TagConfig tag : config.getTag()) {
			Class<Tag> tagClass;
			try {
				tagClass = (Class<Tag>) Class.forName(tag.getTagClass());
				String tagName = StringUtils.isEmpty(prefix) ? tag.getName() : prefix + ":" + tag.getName();
				libMap.put(tagName, tagClass);
			} catch (ClassNotFoundException e) {
				throw new SmileRunException(e);
			}
		}
	}
	
	/**
	 * 注册函数库
	 * 
	 * @param functionMap
	 * @param prefix
	 * @param functionLibName
	 */
	public void registFunction(Map<FunctionKey, Function> functionMap, String prefix, String functionLibName) {
		TagLibConfig config = getFunctionLibConfig(functionLibName);
		for (FunctionConfig function : config.getFunction()) {
			Class functionClass;
			try {
				String functionName ;
				functionClass = (Class) Class.forName(function.getFunctionClass());
				if(Function.class.isAssignableFrom(functionClass)){
					Function f=(Function)ClassTypeUtils.newInstance(functionClass);
					String name=StringUtils.isEmpty(function.getName())?f.getName():function.getName();
					functionName= StringUtils.isEmpty(prefix) ? name : prefix + ":" + name;
					functionMap.put(new FunctionKey(functionName,f.getSupportArgsCount()), f);
				}else{//表态方法函数
					functionName= StringUtils.isEmpty(prefix) ? function.getName() : prefix + ":" + function.getName();
					Class[] typeClass;
					if (StringUtils.notEmpty(function.getArgType())) {
						String[] argType = function.getArgType().split(",");
						typeClass = new Class[argType.length];
						for (int i = 0; i < argType.length; i++) {
							typeClass[i] = ClassTypeUtils.getClassType(argType[i]);
						}
					} else {
						typeClass = new Class[0];
					}
					Method method = MethodUtils.getAnyMethod(functionClass, function.getMethod(), typeClass);
					if (method == null) {
						throw new NullPointerException("can not find a method named " + function.getMethod() + " from " + functionClass + " of types " + function.getArgType());
					}
					Function f = new ConfigurableFunction(function.getName(), method);
					functionMap.put(new FunctionKey(functionName,typeClass.length), f);
				}
				
			} catch (ClassNotFoundException e) {
				throw new SmileRunException(e);
			} catch (BeanException e) {
				throw new SmileRunException(e);
			}
		}
	}

	/**
	 * 注册标签库
	 * 
	 * @param state
	 * @param prefix
	 * @param tagLibName
	 */
	public void registTags(State state, String prefix, String tagLibName) {
		TagLibConfig config = getTagLibConfig(tagLibName);
		for (TagConfig tag : config.getTag()) {
			Class<Tag> tagClass;
			try {
				tagClass = (Class<Tag>) Class.forName(tag.getTagClass());
				String tagName = StringUtils.isEmpty(prefix) ? tag.getName() : prefix + ":" + tag.getName();
				state.registTag(tagName, tagClass);
			} catch (ClassNotFoundException e) {
				throw new SmileRunException(e);
			}
		}
	}

	/**
	 * 获取标签配置
	 * 
	 * @param name
	 * @return
	 */
	private TagLibConfig getTagLibConfig(String name) {
		for (TagLibConfig lib : this.tagLibs) {
			if (name.equals(lib.getName())) {
				return lib;
			}
		}
		throw new SmileRunException("not find tag lib named " + name);
	}

	/**
	 * 获取函数库配置
	 * 
	 * @param name
	 * @return
	 */
	private TagLibConfig getFunctionLibConfig(String name) {
		for (TagLibConfig lib : this.functionLibs) {
			if (name.equals(lib.getName())) {
				return lib;
			}
		}
		throw new SmileRunException("not find function lib named " + name);
	}

	/**
	 * 加载一个配置文件
	 * 
	 * @param is
	 * @throws IOException
	 */
	private void loadConfigFile(InputStream is) throws IOException {
		TagLibConfig config = XmlUtils.parserXml(TagLibConfig.class, is);
		List<TagConfig> tags = config.getTag();
		if (CollectionUtils.notEmpty(tags)) {
			this.tagLibs.add(config);
		}
		// 函数数库
		if (CollectionUtils.notEmpty(config.getFunction())) {
			this.functionLibs.add(config);
		}
	}
}
