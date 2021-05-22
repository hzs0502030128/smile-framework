package org.smile.strate.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.plugin.MethodInterceptor;
import org.smile.strate.StrateExecutor;
import org.smile.strate.config.Config;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.interceptor.ActionInterceptor;
import org.smile.util.StringUtils;

/**
 * 封闭一个package的配置信息
 * @author 胡真山
 * @Date 2016年1月18日
 */
public class PackageElement {
	/**默认的拦截器名称 */
	private String defaultInterceptorName;
	/** 继承的包名 */
	private String[] extendsPackage;
	/** action配置信息*/
	private Map<ActionKey,ActionElement> actions=new HashMap<ActionKey,ActionElement>();
	/**拦截器*/
	private List<MethodInterceptor> interceptors=new LinkedList<MethodInterceptor>();
	/**是否初始化了包中的拦截串*/
	private boolean defaultInterceptorInit=false;
	/**命名空间*/
	private String namespace;
	/**包名称*/
	private String name;
	/**当前action执行者*/
	private StrateExecutor executor;
	/**所属的配置文件*/
	private Config config;
	/**名称与类型映射*/
	private Map<String,ClassElement> classElements=new HashMap<String, ClassElement>();
	/**
	 * 一个包中的跳转,可供此包中所有的action使用
	 * 若action中未配置则使用包中的配置
	 * 跳转的集合
	 */
	private Map<String,ResultConfig> resultMap=new HashMap<String,ResultConfig>();
	/**是否已经继承过了*/
	private boolean extendResult=false;
	
	public PackageElement(String namespace,String name) {
		this.namespace=namespace;
		this.name=name;
		correctNamespace();
	}
	/**
	 * 修下namespace的内容
	 *  正确为 /name/
	 */
	private void correctNamespace() {
		if(StringUtils.notEmpty(this.namespace)) {
			if(this.namespace.charAt(0)!='/') {
				this.namespace='/'+this.namespace;
			}
			if(this.namespace.charAt(namespace.length()-1)!='/') {
				this.namespace+='/';
			}
		}
	}

	public String getConfigFile() {
		return config.getFileName();
	}
	/**
	 * 如有多个http方法则会添加多次
	 * @param action
	 */
	public void addActionElement(ActionElement action){
		String[] httpMethods=action.getHttpMethod();
		if(ArrayUtils.isEmpty(httpMethods)) {
			addActionEelement(new ActionKey(action.getName()), action);
		}else {
			for(String m:httpMethods) {
				addActionEelement(new ActionKey(action.getName(),m), action);
			}
		}
	}
	/**
	 * 往包结构中添加action元素
	 * @param key
	 * @param action
	 */
	private void addActionEelement(ActionKey key,ActionElement action) {
		if(this.actions.containsKey(key)) {
			throw new StrateInitException("action key "+key+" is duplicate defined "+action.getMethod());
		}
		this.actions.put(key, action);
	}
	
	public void addClassElement(ClassElement classElement){
		classElements.put(classElement.getName(),classElement);
	}
	
	public ActionElement getActioneElement(String name) throws NoActionFindException{
		ActionElement action=actions.get(name);
		if(action==null){
			throw new NoActionFindException("not find a action named "+name);
		}
		return action;
	}

	public String getDefaultInterceptorName() {
		return defaultInterceptorName;
	}

	public void setDefaultInterceptorName(String defaultInterceptorName) {
		this.defaultInterceptorName = defaultInterceptorName;
	}

    /**
     * 设置包继承名称
     * @param extendsPackage
     */
	public void setExtendsPackage(String[] extendsPackage) {
		this.extendsPackage = extendsPackage;
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 初始化拦截器信息
	 * @param namespaceActionsMap 所有的名信息
	 * @param interceptorMap 所有的拦截器
	 */
	public void initDefaultInterceptor(Map<String,PackageElement> nameActionsMap,Map<String,List<ActionInterceptor>> interceptorMap){
		/**添加包配置的拦截器*/
		if(StringUtils.notEmpty(defaultInterceptorName)){
			List<ActionInterceptor> defualtList=interceptorMap.get(defaultInterceptorName);
			if(CollectionUtils.notEmpty(defualtList)){
				this.interceptors.addAll(defualtList);
			}
		}
		/**添加继承的拦截器*/
		if(ArrayUtils.notEmpty(extendsPackage)){
			for(String extend:this.extendsPackage){
				PackageElement parentPackageEle=ActionContext.namePackageMap.get(extend);
				if(parentPackageEle==null){
					throw new StrateInitException("继承了不存在的package名称"+extend);
				}
				//先初始化父包拦截器
				if(!parentPackageEle.defaultInterceptorInit){
					parentPackageEle.initDefaultInterceptor(nameActionsMap, interceptorMap);
					parentPackageEle.defaultInterceptorInit=true;
				}
				//把父包的拦截器加入当前
				for(MethodInterceptor i:parentPackageEle.interceptors){
					if(!this.interceptors.contains(i)){
						interceptors.add(i);
					}
				}
			}
		}
	}
	/**继承命名空间*/
	public void extendNamespaceIfEmpty() {
		if(StringUtils.isEmpty(this.namespace)){
			/**添加继承的拦截器*/
			if(ArrayUtils.notEmpty(extendsPackage)){
				//继承第一个
				String extend=extendsPackage[0];
				PackageElement parentPackageEle=ActionContext.namePackageMap.get(extend);
				if(parentPackageEle==null){
					throw new StrateInitException("继承了不存在的package名称"+extend);
				}
				//继承父包的命名空间
				String parentNamespace=parentPackageEle.getNamespace();
				if(StringUtils.isEmpty(parentNamespace)) {
					parentPackageEle.extendNamespaceIfEmpty();
				}else {
					this.namespace=parentNamespace;
				}
			}
		}
	}
	/**
	 * @return 当前包的所有拦截器
	 */
	public List<MethodInterceptor> getInterceptors() {
		return interceptors;
	}


	public String getNamespace() {
		return namespace;
	}


	public Map<ActionKey, ActionElement> getActions() {
		return actions;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public StrateExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(StrateExecutor executor) {
		this.executor = executor;
	}
	
	public Collection<ClassElement> getClassElements(){
		return classElements.values();
	}

	public String[] getExtendsPackage() {
		return extendsPackage;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	/**
	 * 添加一个跳转结果
	 * @param rc
	 */
	public void addResultConfig(ResultConfig rc){
		this.resultMap.put(rc.getName(), rc);
	}
	/**
	 * 先从此包中获取
	 * 没有时从全局获取
	 * @param name
	 * @return
	 */
	public ResultConfig getResultConfig(String name){
		ResultConfig rc=this.resultMap.get(name);
		if(rc==null){
			return  ActionContext.getGlobaResult(name);
		}
		return rc;
	}

	/**
	 * 继承跳转结果配置
	 */
	public void extendResultConfig() {
		if(ArrayUtils.notEmpty(extendsPackage)){
			for(String p:extendsPackage){
				PackageElement pe=ActionContext.namePackageMap.get(p);
				if(!pe.extendResult){//父包没有初化继承先初始化
					pe.extendResultConfig();
				}
				for(ResultConfig rc:pe.resultMap.values()){
					if(!this.resultMap.containsKey(rc.getName())){//没有定义时从父包中继承
						this.resultMap.put(rc.getName(), rc);
					}
				}
			}
		}
		this.extendResult=true;
	}
}
