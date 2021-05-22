package org.smile.strate.action;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smile.collection.CollectionUtils;
import org.smile.reflect.MethodUtils;
import org.smile.strate.StrateExecutor;
import org.smile.strate.config.ActionConfig;
import org.smile.strate.config.ResultConfig;
import org.smile.util.StringUtils;
import org.smile.validate.ValidateElement;
import org.smile.validate.ValidateSupport;
/**
 * 
 * @author 胡真山
 *
 */
public class ActionElement {
	/**
	 * validate 验证方法
	 */
	protected static final String validate_method_name = "validate";
	/**
	 * action的名称 是在strate.xml 文件中配置的name 属性的对应的值
	 */
	private String name;
	/*** 类名 */
	private String clazz;
	/**action对应的方法名称 */
	private String method;
	/**前缀验证方法*/
	private Method validateMethod;
	/**
	 * 跳转的集合
	 */
	private Map<String,ResultConfig> resultMap=new HashMap<String,ResultConfig>();
	/**验证信息*/
	private ValidateElement validateElement;
	/**class 信息*/
	private ClassElement classElement;
	/**包信息*/
	private PackageElement packageElement;
	/**当前action方法使用的执行器*/
	private StrateExecutor executor;
	/**批注信息 解释*/
	private String note;
	/**支持的http方法类型*/
	private String[] httpMethod;
	/**url中的参数名称  例如/find/{name}/{age}*/
	private String[] urlParams;
	
	/**
	 * 配置信息中实例化
	 * @param actionConfig
	 * @param packageElement
	 */
	public ActionElement(ActionConfig actionConfig,PackageElement packageElement){
		this.name=actionConfig.getName();
		this.clazz=actionConfig.getClazz();
		this.method=actionConfig.getMethod();
		this.note=actionConfig.getNote();
		List<ResultConfig> results=actionConfig.getResult();
		if(CollectionUtils.notEmpty(results)){
			for(ResultConfig rc:results){
				resultMap.put(rc.getName(), rc);
			}
		}
		this.packageElement=packageElement;
		this.executor=packageElement.getExecutor();
	}
	/**
	 * 初以化以validate为前缀的验证方法
	 */
	public void initValidateMethod(){
		Class actionClass=this.classElement.getActionClass();
		String validateMethodName=validate_method_name+StringUtils.getFirstCharUpper(method);
		List<Method> validateMethods=MethodUtils.getMethodByName(actionClass,validateMethodName);
		if(validateMethods.size()>1) {
			throw new StrateInitException("Multiple methods exist "+actionClass+" named "+validateMethodName);
		}else if(validateMethods.size()==1){
			this.validateMethod=validateMethods.get(0);
		}
	}
	/**
	 * 实现化一个action配置元素
	 * @param name
	 * @param clazz
	 * @param method
	 * @param packageElement
	 * @param results
	 */
	public ActionElement(String name,String clazz,String method,PackageElement packageElement,List<ResultConfig> results){
		this.name=name;
		this.clazz=clazz;
		this.method=method;
		if(CollectionUtils.notEmpty(results)){
			for(ResultConfig rc:results){
				resultMap.put(rc.getName(), rc);
			}
		}
		this.packageElement=packageElement;
		this.executor=packageElement.getExecutor();
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getClazz() {
		return clazz;
	}


	public void setClazz(String clazz) {
		this.clazz = clazz;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}
	

	public String getNamespace() {
		return this.packageElement.getNamespace();
	}

	public PackageElement getPackageElement() {
		return packageElement;
	}
	
	/**
	 * 获取的返回配置
	 * @param name
	 * @return
	 */
	public ResultConfig getResultConfig(String name){
		ResultConfig result = resultMap.get(name);
		if (result == null) {
			return this.packageElement.getResultConfig(name);
		}
		return result;
	}

	public ClassElement getClassElement() {
		return classElement;
	}

	public void setClassElement(ClassElement classElement) {
		this.classElement = classElement;
	}

	public void setValidateElement(ValidateElement validateElement) {
		this.validateElement = validateElement;
	}
	/**
	 * 执行验证方法
	 * @param validateTarget
	 * @return
	 * @throws Exception
	 */
	public boolean validate(ValidateSupport validateTarget) throws Exception{
		if(this.validateElement==null){
			return true;
		}
		return this.validateElement.validate(validateTarget);
	}

	public StrateExecutor getExecutor() {
		return executor;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String[] getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String[] httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	public String[] getUrlParams() {
		return urlParams;
	}
	
	public void setUrlParams(String[] urlParams) {
		this.urlParams = urlParams;
	}
	/**
	 * 获取action的验证方法
	 * @return
	 */
	public Method getValidateMethod() {
		return validateMethod;
	}
	/**
	 * 获以后置方法
	 * @return
	 */
	public Method getAfterMethod() {
		return this.classElement.getAfterMethod();
	}
	
	public Class getActionClass() {
		return this.classElement.getActionClass();
	}
	
}
