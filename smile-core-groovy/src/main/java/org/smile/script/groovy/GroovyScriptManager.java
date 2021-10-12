package org.smile.script.groovy;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceConnector;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.IOException;
import java.util.Map;

import org.smile.Smile;
import org.smile.commons.SmileRunException;
/**
 * groovy script 执行
 * @author 胡真山
 *
 */
public class GroovyScriptManager{
	
	protected GroovyScriptEngine engine;
	
	public GroovyScriptManager(ResourceConnector resourceConnector){
		this.engine=new GroovyScriptEngine(resourceConnector);
		this.engine.getConfig().setSourceEncoding(Smile.ENCODE);
	}
	/**
	 * 从方法目录初始化脚本引擎
	 * @param roots
	 */
	public GroovyScriptManager(String[] roots){
		try {
			this.engine=new GroovyScriptEngine(roots);
			this.engine.getConfig().setSourceEncoding(Smile.ENCODE);
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}
	/***
	 * 设置源代码的编码
	 * @param encoding
	 */
	public void setSourceEncoding(String encoding){
		this.engine.getConfig().setSourceEncoding(encoding);
	}
	
	/**
	 * 执行一个groovy脚本
	 * @param scriptName
	 * @param binding
	 * @return
	 */
	public Object excute(String scriptName,Binding binding){
        try {
            return engine.run(scriptName, binding);
        } catch (ResourceException e) {
            throw new SmileRunException(e);
        } catch (ScriptException e) {
            throw new SmileRunException(e);
        }
	}
	/**
	 * @param scriptName
	 * @return
	 */
	public Object excute(String scriptName){
		return excute(scriptName, new Binding());
	}
	/**
	 * 执行脚本 
	 * @param scriptName 
	 * @param paramter  参数
	 * @return
	 */
	public Object excute(String scriptName,Map<String,Object> paramter){
		Binding binding=new Binding();
		for(Map.Entry<String,Object> entry:paramter.entrySet()){
			binding.setVariable(entry.getKey(), entry.getValue());
		}
		return excute(scriptName,binding );
	}
}
