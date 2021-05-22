package org.smile.strate.action;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.smile.collection.ArrayUtils;
import org.smile.collection.UnmodifiableMap;
import org.smile.commons.SmileRunException;
import org.smile.commons.ann.Note;
import org.smile.reflect.MethodUtils;
import org.smile.strate.StrateException;
import org.smile.util.StringUtils;
import org.smile.validate.ValidateElement;
import org.smile.validate.ValidateElementUtils;

/**
 * 保存了一个类中的所有action
 * 
 * @author 胡真山
 * @Date 2016年1月20日
 */
public class ClassElement {
	/**
	 * 处理完action方法后调用的方法
	 */
	protected static final String after_method_name = "afterAction";
	/**
	 * 以action名称映射
	 */
	private Map<ActionKey, ActionElement> namedActions = new HashMap<ActionKey, ActionElement>();
	/**
	 * 以方法名映射
	 */
	private Map<String,ActionElement> methodActions = new HashMap<String,ActionElement>();
	/** 保存action的类 */
	private Class actionClass;
	/**类的名称,也可能是配置的IOC容器中的名称*/
	private String name;
	/**通用action执行后置方法*/
	private Method afterMethod;
	
	
	public ClassElement(String name){
		this.name=name;
	}

	public Map<ActionKey, ActionElement> getActions() {
		return new UnmodifiableMap<>(namedActions);
	}

	/**
	 * action的类型
	 * @return
	 */
	public Class getActionClass() {
		return actionClass;
	}
	/**
	 * 是否存在方法名的action
	 * @param method
	 * @return
	 */
	public boolean existsActionByMethodName(String method){
		return methodActions.containsKey(method);
	}

	/**
	 * 设置类元素的实现类
	 * @param clazz
	 */
	public void setActionClass(Class clazz) {
		this.actionClass = clazz;
		initActionValidateMethod();
		this.afterMethod = MethodUtils.getMethod(this.actionClass, after_method_name);
	}
	/**
	 * 初始化验证方法
	 */
	private void initActionValidateMethod() {
		for(ActionElement ae:namedActions.values()) {
			ae.initValidateMethod();
		}
	}

	public void addActionElement(ActionElement action) {
		String[] httpMethods=action.getHttpMethod();
		if(ArrayUtils.isEmpty(httpMethods)) {
			addActionEelement(new ActionKey(action.getName()), action);
		}else {
			for(String m:httpMethods) {
				addActionEelement(new ActionKey(action.getName(),m), action);
			}
		}
		
		if(StringUtils.notEmpty(action.getMethod())){
			this.methodActions.put(action.getMethod(),action);
		}
	}
	/**
	 * 添加一个action元素
	 * @param key
	 * @param action
	 */
	private void addActionEelement(ActionKey key,ActionElement action) {
		if(this.namedActions.containsKey(key)) {
			throw new StrateInitException("action key "+key+" is duplicate defined");
		}
		this.namedActions.put(key, action);
	}

	/**
	 * 以方法名判断是否存在action元素
	 * @param methodName
	 * @return
	 */
	public boolean hasActionMethod(String methodName) {
		return this.methodActions.containsKey(methodName);
	}
	
	/***
	 * 以方法名名称来获取action配置元素
	 * @param method
	 * @return
	 */
	public ActionElement getActionElementByMethod(String method){
		return methodActions.get(method);
	}

	/**
	 * 初始化注解验证配置
	 * @throws StrateException
	 * */
	public void initValidateElement() throws StrateException {
		for (ActionElement actionEle : namedActions.values()) {
			try{
				Method method=actionEle.getExecutor().getActionMethod(actionClass, actionEle.getMethod());
				ValidateElement ve=ValidateElementUtils.checkAnnotationValidate(method);
				if(ve!=null){
					actionEle.setValidateElement(ve);
				}
				Note note=method.getAnnotation(Note.class);
				if(note!=null){
					actionEle.setNote(note.value());
				}
			}catch(SmileRunException e){
				throw new StrateInitException("初始化 action "+actionEle.getName()+"失败, package "+actionEle.getPackageElement().getName()+", filename "+actionEle.getPackageElement().getConfigFile(), e);
			}
		}
	}
	/**
	 * 类元素配置名称 可能是类名 也可能是IOC容器中的名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取当前action类的通用后置方法
	 * @return
	 */
	public Method getAfterMethod() {
		return afterMethod;
	}
	
	
	
}
