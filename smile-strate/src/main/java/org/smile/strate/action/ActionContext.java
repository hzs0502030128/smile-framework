package org.smile.strate.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.annotation.AnnotationUtils;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.commons.ExceptionUtils;
import org.smile.config.BeansConfig;
import org.smile.config.BeansConfigProperties;
import org.smile.config.IncludeConfig;
import org.smile.http.HttpMethod;
import org.smile.io.ResourceUtils;
import org.smile.log.LoggerHandler;
import org.smile.reflect.ClassTypeUtils;
import org.smile.strate.Strate;
import org.smile.strate.StrateException;
import org.smile.strate.StrateExecutor;
import org.smile.strate.ann.ActionPackage;
import org.smile.strate.ann.Result;
import org.smile.strate.config.ActionConfig;
import org.smile.strate.config.Config;
import org.smile.strate.config.ConstantConfig;
import org.smile.strate.config.DefaultInterceptorConfig;
import org.smile.strate.config.ExceptionConfig;
import org.smile.strate.config.ExecutorConfig;
import org.smile.strate.config.GlobaExceptionMappingConfig;
import org.smile.strate.config.GlobalResultConfig;
import org.smile.strate.config.InterceptorConfig;
import org.smile.strate.config.InterceptorRefConfig;
import org.smile.strate.config.InterceptorStackConfig;
import org.smile.strate.config.InterceptorsConfig;
import org.smile.strate.config.PackageConfig;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.ActionURIParser;
import org.smile.strate.dispatch.StrateDispatcher;
import org.smile.strate.form.StrateConverter;
import org.smile.strate.interceptor.ActionInterceptor;
import org.smile.util.ClassScaner;
import org.smile.util.Properties;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;
import org.smile.util.XmlUtils;

/**
 * 这是一个strate的核数据的容器  
 * 配置信息都在此类中
 * 这此配置中是一个命名空间与包名是一对一的关系
 * @author 胡真山
 */
public class ActionContext implements LoggerHandler{
	/**包继承配置分隔*/
	private static final String PACKAGE_EXTENDS_SPLIT=",";
	/**用于保存常量*/
	public static Strate strate;
	/**主配置文件*/
	private static Config RootConfig;
	/**
	 * 保存文件中配置的所有的bean名字与类名对应
	 */
	protected  static Map<String,ResultConfig> globaResultMap=new HashMap<String, ResultConfig>();
	/**
	 * 异常名称与反回结果映射
	 */
	protected  static Map<Class,ResultConfig> globaExceptionMap=new LinkedHashMap<Class, ResultConfig>();
	/**
	 * 以命名空间为键的package信息
	 */
	protected  static Map<String,Map<ActionKey,ActionElement>> namespaceActionsMap=new HashMap<String, Map<ActionKey,ActionElement>>();
	/**
	 * 以包名称为键的package信息
	 */
	protected  static Map<String,PackageElement> namePackageMap=new HashMap<String, PackageElement>();
	/**
	 * 所有的拦截器名称与所包括的拦截器遇敌
	 */
	protected  static Map<String,List<ActionInterceptor>> interceptorMap=new HashMap<String,List<ActionInterceptor>>();
	/**缓存起所有的action的class 配置 
	 * */
	protected static Map<String,ClassElement> allActionClazz=new HashMap<String,ClassElement>();
	/**
	 * 默认的配置文件
	 */
	protected  static  final String configFileName="strate.xml";
	/**
	 * 当前线程的request 与 response
	 */
	protected static ThreadLocal<RequestAndResponse> reqAndResp=new ThreadLocal<RequestAndResponse>();
	/**
	 * 当前servletContext
	 */
	private static ServletContext servletContext;
	
	private static BeansConfigProperties beanFactory=new BeansConfigProperties();
	/**
	 * 初始化配置信息
	 * @throws StrateException 
	 */
	public static void init(ServletContext context) throws StrateException{
		try{
			servletContext=context;
			InputStream configInput=ResourceUtils.getResourceAsStream(configFileName);
			if(configInput==null){
				String path=context.getRealPath("/")+"/WEB-INF/classes/"+configFileName;
				try {
					configInput=new FileInputStream(path);
				} catch (FileNotFoundException e) {
					throw new StrateException(e);
				}
			}
			Set<Config> allConfig=new LinkedHashSet<Config>();
			//解析配置文件
			RootConfig=XmlUtils.parserXml(Config.class, configInput);
			RootConfig.setFileName(configFileName);
			allConfig.add(RootConfig);
			registBeanConfig(RootConfig.getBeans());
			/**处理包含的文件*/
			List<IncludeConfig> includes=RootConfig.getInclude();
			if(RootConfig.getExecutor()==null){//没有配置的时候默认初始化一个
				RootConfig.setExecutor(new ExecutorConfig());
			}
			
			if(CollectionUtils.notEmpty(includes)){
				for(IncludeConfig include:includes){
					String filename=include.getFile();
					try{
						InputStream fileIs=ActionContext.class.getClassLoader().getResourceAsStream(filename);
						Config includeConfig=XmlUtils.parserXml(Config.class, fileIs);
						includeConfig.setFileName(filename);
						registBeanConfig(includeConfig.getBeans());
						if(includeConfig.getExecutor()==null){
							//单个文件没有配置时使用主文件的executor
							includeConfig.setExecutor(RootConfig.getExecutor());
						}
						allConfig.add(includeConfig);
					}catch(Exception e){
						throw new StrateInitException("读取文件 strate.xml include file "+filename+" throw a exception ",e);
					}
				}
			}
			initConstants(allConfig);
			initInterceptorsConfig(allConfig);
			initInterceptorStackConfig(allConfig);
			//初始化出所有配置文件中的action
			for(Config cfg:allConfig){
				initResultAndAction(cfg);
			}
			//实始化注解action信息
			initActionScanerResult(allConfig);
			initGlobaExceptionMapping(allConfig);
			for(PackageElement packageEle:namePackageMap.values()){
				//继承命名空间
				packageEle.extendNamespaceIfEmpty();
				//实始化包的拦截器
				packageEle.initDefaultInterceptor(namePackageMap, interceptorMap);
				putActionToNamespaceMap(packageEle);
			}
			
			initActionConfig(context);
			//
			initExecutor();
			
			initClassElements();
			//包结果继承
			for(PackageElement pe:namePackageMap.values()){
				pe.extendResultConfig();
			}
			//url解析配置
			ActionURIParser urlParser=getActionUrlParser(RootConfig);
			if(urlParser!=null) {//设置主配置的urlParser为配置的parser
				StrateDispatcher.DefaultParser=urlParser;
			}
			logger.info("strate配置文件加载完成:["+configFileName+"]"+namePackageMap.keySet());
		} catch (ConvertException e) {
			throw new StrateInitException("初始化 strate 失败",e);
		} catch (ClassNotFoundException e) {
			throw new StrateInitException("初始化 strate 失败",e);
		}
	}
	
	/**
	 * 注入一个文件中的配置的bean配置
	 * @param beansConfig
	 */
	private static void registBeanConfig(BeansConfig beansConfig) {
		if(beansConfig==null) {
			return ;
		}
		beanFactory.regist(beansConfig);
	}
	/**
	 * 注解化action注解结果 
	 * 每一个配置文件都可以有注解扫描路径
	 * @param allConfig
	 */
	private static void initActionScanerResult(Set<Config> allConfig) {
		for(Config cfg:allConfig){
			String ascfg= cfg.getActionScanner();
			if(StringUtils.notEmpty(ascfg)){
				List<String> packageStrs=RegExp.DEF_STR_SPLIT.splitAndTrimNoBlack(ascfg);
				ClassScaner scaner=new ClassScaner();
				for(String str:packageStrs){
					initActionScannerPackage(scaner,str);
				}
			}
		}
	}
	/**
	 * 获取执行器的实例
	 * @param config
	 * @return
	 * @throws BeanException
	 */
	private static StrateExecutor getExecutorInstance(Config config){
		ExecutorConfig ec=config.getExecutor();
		if(StringUtils.notEmpty(ec.getRef())) {//使用引用beans配置时
			try {
				return beanFactory.getBean(ec.getRef());
			} catch (BeanException e) {
				throw new StrateInitException("init strate executor error ",e);
			}
		}
		return ec.getInstance();
	}
	/**
	 * 获取默认的执行器
	 * @return
	 */
	public static StrateExecutor getDefaultExecutor() {
		return getExecutorInstance(RootConfig);
	}
	
	/**
	 * 获取配置文件中的url解析器
	 * @param config
	 * @return
	 */
	private static ActionURIParser getActionUrlParser(Config config) {
		//初始化url解析器
		if(config.getActionUrlParser()!=null){//配置了url解析
			ActionURIParser parser=null;
			try {
				if(config.getActionUrlParser().getRef()!=null) {
					parser=beanFactory.getBean(config.getActionUrlParser().getRef());
				}else {
					parser = ClassTypeUtils.newInstance(config.getActionUrlParser().getClazz());
				}
			} catch (BeanException e) {
				logger.warn(ExceptionUtils.getExceptionMsg(e));
			}
			return parser;
		}
		return null;
	}
	/**
	 * 处理一个包下的action注解
	 * @param str 
	 */
	private static void initActionScannerPackage(ClassScaner scaner,String str) {
		Set<Class<?>> classSet=scaner.getClasses(StringUtils.trim(str));
		if(!classSet.isEmpty()){
			for(Class<?> clazz:classSet){
				ActionPackage apkg=AnnotationUtils.getAnnotation(clazz,ActionPackage.class);
				if(apkg!=null){
					String packageName=apkg.name();
					if(StringUtils.isEmpty(packageName)){//没有设置包名的时候使用类名做为包名
						packageName=clazz.getName();
					}
					PackageElement packageElement=new PackageElement(apkg.namespace(),packageName);
					addPackage(packageElement);
					//包继承名称
					if(StringUtils.notEmpty(apkg.extend())){
						String[] strs=apkg.extend().split(PACKAGE_EXTENDS_SPLIT);
						packageElement.setExtendsPackage(strs);
						if(StringUtils.isEmpty(packageElement.getNamespace())){//没有设置命名空间时使用父包的命名空间
							//设置继承父包的命名空间
							PackageElement parentPackage=namePackageMap.get(strs[0]);
							if(parentPackage!=null){
								packageElement.setNamespace(parentPackage.getNamespace());
							}
						}
					}
					//继承父包的executor
					String[] extend=packageElement.getExtendsPackage();
					if(ArrayUtils.notEmpty(extend)){
						PackageElement parentPackage=namePackageMap.get(extend[0]);
						if(parentPackage!=null){
							packageElement.setExecutor(parentPackage.getExecutor());
							packageElement.setConfig(parentPackage.getConfig());
						}else {
							throw new StrateInitException(packageElement.getName()+" extend package "+extend[0]+" is not exists");
						}
					}else {
						packageElement.setExecutor(getDefaultExecutor());
						packageElement.setConfig(RootConfig);
					}
					//处理包下的action
					initClassAnnActions(clazz, packageElement);
					//默认拦截器
					String defalutInterceptor=apkg.interceptorRef();
					if(StringUtils.notEmpty(defalutInterceptor)){
						packageElement.setDefaultInterceptorName(defalutInterceptor);
					}
					//实始化包跳转
					Result[] results=apkg.results();
					if(ArrayUtils.notEmpty(results)){
						for(Result r:results){
							packageElement.addResultConfig(new ResultConfig(r));
						}
					}
				}
			}
		}
	}
	/**
	 * 	实始化一个类的注解action
	 * @param clazz
	 * @param packageElement
	 */
	private static void initClassAnnActions(Class clazz,PackageElement packageElement){
		//处理包下的action
		Method[] methods=clazz.getDeclaredMethods();
		for(Method m:methods){
			ActionElement actionElement=createActionElelemntByAnnotation(packageElement, clazz, m);
			if(actionElement!=null) {
				initActionElement(packageElement, actionElement, clazz.getName());
			}
		}
	}
	/**
	 * 从注解信息中创建一个action元素
	 * @param m
	 * @return
	 */
	private static ActionElement createActionElelemntByAnnotation(PackageElement packageElement,Class clazz,Method m) {
		org.smile.strate.ann.Action action=AnnotationUtils.getAnnotation(m,org.smile.strate.ann.Action.class);
		String actionName=null;
		String[] httpMethod=null;
		String[] paramNames=null;
		if(action!=null) {
			actionName=action.name();
			if(StringUtils.isEmpty(actionName)) {
				actionName=m.getName();
			}
			int idx=actionName.indexOf('{');
			if(idx>0) {
				String paramUrl=actionName.substring(idx);
				String[] args=paramUrl.split("/");
				paramNames=new String[args.length];
				for(int i=0;i<args.length;i++) {
					paramNames[i]=StringUtils.trim(args[i].substring(1,args[i].length()-1));
				}
				actionName=actionName.substring(0,idx-1);
			}
			//支持的http method 
			HttpMethod[] method=action.method();
			httpMethod=new String[method.length];
			for(int i=0;i<method.length;i++) {
				httpMethod[i]=method[i].name();
				i++;
			}
			Result[] results=action.results();
			List<ResultConfig> resultConfigs=new LinkedList<ResultConfig>();
			for(Result r:results){
				resultConfigs.add(new ResultConfig(r));
			}
			if(StringUtils.isEmpty(actionName)) {
				actionName=m.getName();
			}
			ActionElement actionElement=new ActionElement(actionName,clazz.getName(),m.getName(),packageElement,resultConfigs);
			actionElement.setHttpMethod(httpMethod);
			actionElement.setUrlParams(paramNames);
			return actionElement;
		}
		return null;
	}
	
	/**把actionElement添加到包结构中
	 **/
	private static void initActionElement(PackageElement packageElement,ActionElement actionElement,String className){
		packageElement.addActionElement(actionElement);
		//以class为key缓存起所有的actionelement
		ClassElement clazzElement=allActionClazz.get(className);
		if(clazzElement==null){
			clazzElement=new ClassElement(className);
			allActionClazz.put(className, clazzElement);
			packageElement.addClassElement(clazzElement);
		}
		clazzElement.addActionElement(actionElement);
		actionElement.setClassElement(clazzElement);
	}
	/**
	 *	把一个包元素中的action注入到命名空间中
	 * @param packageEle
	 */
	private static void putActionToNamespaceMap(PackageElement packageEle){
		//一个命名空间中的action
		Map<ActionKey,ActionElement> oneNamespaceActions=namespaceActionsMap.get(packageEle.getNamespace());
		if(oneNamespaceActions==null){
			oneNamespaceActions=new HashMap<ActionKey, ActionElement>();
			namespaceActionsMap.put(packageEle.getNamespace(), oneNamespaceActions);
		}
		//一个包中的action元素
		Map<ActionKey,ActionElement> pakageActionMap =packageEle.getActions();
		for(Map.Entry<ActionKey,ActionElement> entry:pakageActionMap.entrySet()){
			ActionElement ae=entry.getValue();
			if(oneNamespaceActions.containsKey(entry.getKey())){
				throw new StrateInitException("重复的action名称"+entry.getKey()+" 在空间 "+ae.getNamespace()+",method:"+ae.getMethod());
			}
			oneNamespaceActions.put(entry.getKey(), ae);
		}
	}
	/**
	 * 初始化出所有的拦截器 
	 * 并添加到 全局map中
	 * @param configList
	 * @return
	 */
	private static void initInterceptorsConfig(Collection<Config> configList){
		for(Config config:configList){
			InterceptorsConfig interceptors=config.getInterceptors();
			if(interceptors!=null){
				List<InterceptorConfig> interceptorList=interceptors.getInterceptor();
				if(CollectionUtils.notEmpty(interceptorList)){
					for(InterceptorConfig c:interceptorList){
						String name=c.getName();
						if(interceptorMap.containsKey(name)){
							throw new StrateInitException("Duplicate interceptor name "+name+" class "+c.getClazz());
						}
						try {
							ActionInterceptor i=(ActionInterceptor) Class.forName(c.getClazz()).newInstance();
							interceptorMap.put(name, CollectionUtils.linkedList(i));
						} catch (Exception e) {
							throw new StrateInitException("init interceptor named "+name+" class "+c.getClazz()+" error "+config.getFileName(),e);
						}
					}
				}
			}
		}
	}
	/**
	 * 初始化出所有的拦截器栈
	 * @param configList
	 */
	private static void initInterceptorStackConfig(Set<Config> configList){	
		for(Config config:configList){
			InterceptorsConfig interceptors=config.getInterceptors();
			if(interceptors!=null){
				List<InterceptorStackConfig> interceptorList=interceptors.getInterceptorStatck();
				if(CollectionUtils.notEmpty(interceptorList)){
					for(InterceptorStackConfig c:interceptorList){
						String name=c.getName();
						if(interceptorMap.containsKey(name)){
							throw new StrateInitException("Duplicate interceptor-stack name "+name+" in file "+config.getFileName());
						}
						List<InterceptorRefConfig> refs=c.getInterceptorRefs();
						if(CollectionUtils.notEmpty(refs)){
							List<ActionInterceptor> refList=new LinkedList<ActionInterceptor>();
							for(InterceptorRefConfig ref:refs){
								List<ActionInterceptor> subInterceptors=interceptorMap.get(ref.getName());
								if(subInterceptors==null){
									throw new StrateInitException(" not has a interceptor named "+ref.getName()+" in file "+config.getFileName());
								}
								refList.addAll(subInterceptors);
							}
							interceptorMap.put(name, refList);
						}
					}
				}
			}
		}
	}
	/**
	 * 初始化常量信息
	 * @param allConfig
	 * @throws ConvertException
	 */
	private static void initConstants(Set<Config> allConfig) throws ConvertException{
		Properties p=new Properties();
		for(Config  c:allConfig){
			List<ConstantConfig> list=c.getConstant();
			if(CollectionUtils.notEmpty(list)){
				for(ConstantConfig cc:list){
					p.setProperty(cc.getName(), cc.getValue());
				}
			}
		}
		p.pushToStaticExpFields(ActionContext.class);
	}
	/**
	 * 初始化异常返回信息
	 * @param allConfig
	 * @throws ClassNotFoundException 
	 */
	private static  void initGlobaExceptionMapping(Set<Config> allConfig) throws ClassNotFoundException {
		for(Config  config:allConfig){
			GlobaExceptionMappingConfig mapping=config.getGlobaExceptionMapping();
			if(mapping!=null){
				List<ExceptionConfig> exceptions=mapping.getExceptions();
				if(CollectionUtils.notEmpty(exceptions)){
					for(ExceptionConfig exc:exceptions){
						ResultConfig rc=globaResultMap.get(exc.getResult());
						if(rc==null){
							throw new StrateInitException("not a globa result named "+exc.getResult()+" exception mapping "+exc.getException()+" in file "+config.getFileName());
						}
						globaExceptionMap.put(Class.forName(exc.getException()), rc);
					}
				}
			}
		}
	}
	/**
	 * 解析一个配置文件中的所有action标签
	 * @param config 一个配置
	 */
	private static void initResultAndAction(Config config){
		initGlobaResult(config);
		initPackage(config);
	}
	/***
	 * 初始化
	 * 全局返回结果
	 * @param config
	 */
	private static void initGlobaResult(Config config){
		GlobalResultConfig resultConfig= config.getGlobaResult();
		if(resultConfig!=null){
			List<ResultConfig> results=resultConfig.getResult();
			if(CollectionUtils.notEmpty(results)){
				for(ResultConfig result:results){
					globaResultMap.put(result.getName(), result);
				}
			}
		}
	}
	/**
	 * 初始化一个package标签
	 * @param config
	 */
	private static void addPackage(PackageElement packageElement){
		if(namePackageMap.containsKey(packageElement.getName())){
			throw new StrateInitException("重复的  package name "+packageElement.getName());
		}
		namePackageMap.put(packageElement.getName(),packageElement);
	}
	/**
	 * 初始化一个配置文件中的包
	 * @param config
	 */
	private static void initPackage(Config config){
		List<PackageConfig> packageConfigs=config.getPackages();
		if(CollectionUtils.notEmpty(packageConfigs)){
			for(PackageConfig pc:packageConfigs){
				List<ActionConfig> actions=pc.getAction();
				PackageElement packageElement=new PackageElement(pc.getNamespace(),pc.getName());
				packageElement.setConfig(config);
				packageElement.setExecutor(getExecutorInstance(config));
				addPackage(packageElement);
				//处理包下的action
				if(CollectionUtils.notEmpty(actions)){
					ActionElement actionElement;
					for(ActionConfig ac:actions){
						actionElement=new ActionElement(ac,packageElement);
						initActionElement(packageElement, actionElement, ac.getClazz());
					}
				}
				//默认拦截器
				DefaultInterceptorConfig dic=pc.getDefaultInterceptor();
				if(dic!=null){
					packageElement.setDefaultInterceptorName(dic.getName());
				}
				//包继承名称
				if(StringUtils.notEmpty(pc.getExtend())){
					String[] strs=pc.getExtend().split(",");
					packageElement.setExtendsPackage(strs);
				}
				//初始化跳转结果
				List<ResultConfig> resultList=pc.getResult();
				if(CollectionUtils.notEmpty(resultList)){
					for(ResultConfig rc:resultList){
						packageElement.addResultConfig(rc);
					}
				}
			}
		}
	}
	/**
	 * 根据异常的类型查找返回的结果配置
	 * @param exception
	 * @return
	 */
	public static ResultConfig getExceptionResult(Class exception){
		for(Map.Entry<Class, ResultConfig> entry:globaExceptionMap.entrySet()){
			if(entry.getKey().isAssignableFrom(exception)){
				return entry.getValue();
			}
		}
		return null;
	}
	/**
	 * 全局返回名称与配置
	 * @param name
	 * @return
	 */
	public static ResultConfig getGlobaResult(String name){
		return globaResultMap.get(name);
	}
	/**
	 * 以命名空间获取package信息
	 * @param namespace  命名空间
	 * @return
	 * @throws NoActionFindException  命名空间不存在时
	 */
	public static ActionElement getActionElement(String namespace,String actionName) throws NoActionFindException{
		Map<ActionKey,ActionElement> actionMap= namespaceActionsMap.get(namespace);
		if(actionMap==null){
			throw new NoActionFindException("not find action namespace "+namespace);
		}
		return actionMap.get(new ActionKey(actionName));
	}
	/**
	 * 以命名空间获取package信息
	 * @param namespace  命名空间
	 * @return
	 * @throws NoActionFindException  命名空间不存在时
	 */
	public static Map<ActionKey,ActionElement> getActionMap(String namespace) throws NoActionFindException{
		Map<ActionKey,ActionElement> actionMap= namespaceActionsMap.get(namespace);
		if(actionMap==null){
			throw new NoActionFindException("not find action namespace "+namespace);
		}
		return actionMap;
	}
	/***
	 * 所有的action类配置信息
	 * @return
	 */
	public static Map<String, ClassElement> getAllActionClazz() {
		return allActionClazz;
	}
	
	/**
	 * 初始化action配置文件的转换器
	 * @param beanhandler 用于获取action的实例
	 * 		  有可能配置的class是IOC容器的bean ID 所以必须通过实例来确认类型
	 * @param context
	 * @throws StrateException
	 */
	private static void initActionConfig(ServletContext context) throws StrateException {
		for(PackageElement pe:namePackageMap.values()){
			Collection<ClassElement> ces=pe.getClassElements();
			StrateExecutor executor=pe.getExecutor();
			for(ClassElement ce:ces){
				Action action =executor.getActionBean(ce.getName(), context);
				Class clazz = action.getClass();
				ce.setActionClass(clazz);
			}
		}
	}
	/**
	 * 实始化所有的executor实例相关信息
	 */
	private static void initExecutor(){
		for(PackageElement packageEle:namePackageMap.values()){
			StrateExecutor executor=packageEle.getExecutor();
			for(ClassElement ce:packageEle.getClassElements()) {
				executor.initOnClassElement(ce);
			}
		}
	}
	
	private static void initClassElements() throws StrateException{
		for(ClassElement ce:allActionClazz.values()){
			StrateConverter.initActionConvertConfig(ce.getActionClass());
			ce.initValidateElement();
		}
	}
	
	public static ServletContext getServletContext() {
		return servletContext;
	}
	
	public static HttpServletRequest getRequest(){
		return reqAndResp.get().getRequest();
	}
	
	public static HttpServletResponse getResponse(){
		return reqAndResp.get().getResponse();
	}
	
	public static RequestAndResponse getRequestAndResponse(){
		return reqAndResp.get();
	}
	
	/**
	 * 设置当前线程的请求信息
	 * @param request
	 * @param response
	 */
	public static final void setRequestAndResponse(HttpServletRequest request,HttpServletResponse response,HttpMethod method){
		reqAndResp.set(new RequestAndResponse(request,response,method));
	}
	/**
	 * 移除请求信息
	 */
	public static final void romoveRequestAndResponse(){
		reqAndResp.remove();
	}
	/**
	 * 所有的包配置
	 * @return
	 */
	public static final Collection<PackageElement> getAllPackage(){
		return namePackageMap.values();
	}
	/**
	 * 所有的action类配置信息
	 * @return
	 */
	public static final Collection<ClassElement> getAllClassElements(){
		return allActionClazz.values();
	}
	
	public static BeansConfigProperties getBeansConfig() {
		return beanFactory;
	}
}
