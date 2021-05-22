package org.smile.script;

import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.smile.beans.BeanUtils;
import org.smile.collection.CollectionUtils;
import org.smile.collection.ThreadLocalMap;
import org.smile.commons.SmileRunException;
import org.smile.expression.Context;
import org.smile.expression.ExpressionEngine;
import org.smile.log.LoggerHandler;

/**
 *     角本语言解析器
 * @author 胡真山
 * 2015年11月9日
 */
public class ScriptExecutor implements LoggerHandler, Executor,ExpressionEngine {
	/**
	 * 单属性对象变量名
	 */
	public static final String SINGLE_PARA_VAR = "value";
	
	private static final String RESULT_VAR="_SCRIPT_RESULT_VAR_";
	
	private static ScriptEngineManager manager = new ScriptEngineManager();
	/**
	 * 当前的脚本语言引擎
	 */
	private ScriptEngine engine;
	/**
	 * 用于保存设置的返回变量名
	 */
	private Map<String,Object> varMap=new ThreadLocalMap<String,Object>();
	
	private String name;

	/**
	 * 
	 * @param name 脚本引擎的名称
	 */
	public ScriptExecutor(String name) {
		engine = manager.getEngineByName(name);
		this.name=name;
		if (engine == null) {
			throw new SmileRunException("can find a script engine by name " + name);
		}
	}

	/**
	 * 执行一段脚本 在脚本中必须把结果赋值结一个名为result的变量
	 * @param script 脚本语句
	 * @param params 传入参数
	 * @return 返回结果  
	 */
	public Object execute(String script, Map<String,Object> params) {
		try {
			Bindings bindings=this.engine.createBindings();
			if(CollectionUtils.notEmpty(params)){
				bindings.putAll(params);
			}
			Object resValue=engine.eval(script,bindings);
			String resultVar=(String)varMap.get(RESULT_VAR);
			if(resultVar!=null){
				return bindings.get(resultVar);
			}
			return resValue;
		} catch (ScriptException e) {
			throw new ScriptRunException("execute script error:" + script + params, e);
		}finally{
			varMap.clear();
		}
	}

	/**
	 * @param script
	 * @param beanParams 可以是bean类型 对象
	 * @return
	 */
	public Object execute(String script, Object beanParams) {
		Map<String,Object> map = null;
		if (beanParams instanceof Map) {
			map = (Map<String,Object>) beanParams;
		} else if (beanParams != null) {
			try {
				map = BeanUtils.mapFromBean(beanParams);
				if (map.isEmpty()) {
					map.put(SINGLE_PARA_VAR, beanParams);
				}
			} catch (Exception e) {
				throw new SmileRunException(e);
			}
		}
		return execute(script, map);
	}

	/**
	 * 没有参数的语句执行
	 * @param script
	 * @return
	 */
	public Object execute(String script) {
		return execute(script, null);
	}

	/**
	 *      初始化参数  以后每次执行都会用到的参数
	 * @param params
	 */
	public void initEngineGlobal(Map<String, ? extends Object> params) {
		if (params != null) {
			Bindings bindings=this.engine.getBindings(ScriptContext.GLOBAL_SCOPE);
			for (Map.Entry<String, ? extends Object> obj : params.entrySet()) {
				bindings.put(obj.getKey(), obj.getValue());
			}
		}
	}

	/**
	 *      设置全局变量值
	 * @param key
	 * @param value
	 */
	public void setGlobalValue(String key, Object value) {
		Bindings bindings=this.engine.getBindings(ScriptContext.GLOBAL_SCOPE);
		bindings.put(key, value);
	}
	
	@Override
	public void setResultVar(String resultVar) {
		this.varMap.put(RESULT_VAR, resultVar);
	}

	/**
	 * 脚本语言
	 * @return
	 */
	public String getLanguage() {
		return this.engine.getFactory().getLanguageName();
	}

	public String getName() {
		return name;
	}

	@Override
	public Object evaluate(Context context, String expression) {
		try {
			Bindings bindings=null;
			if(context instanceof Bindings) {
				bindings=(Bindings)context;
			}else {
				bindings=this.engine.createBindings();
				Set<String> contextKeys=context.keys();
				if(CollectionUtils.notEmpty(contextKeys)){
					for(String key:contextKeys) {
						bindings.put(key,context.get(key));
					}
				}
			}
			Object resValue=engine.eval(expression,bindings);
			return resValue;
		} catch (ScriptException e) {
			throw new ScriptRunException("execute script error:" + expression, e);
		}finally{
			varMap.clear();
		}
	}

	@Override
	public Context createContext(Object rootValue) {
		return new ContextBindings(this.engine.getContext(),rootValue);
	}
	
}
