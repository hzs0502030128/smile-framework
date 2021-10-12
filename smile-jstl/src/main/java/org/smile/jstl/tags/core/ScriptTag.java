
package org.smile.jstl.tags.core;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.log.LoggerHandler;
import org.smile.script.ScriptType;
import org.smile.script.ScriptExecutor;
import org.smile.util.StringUtils;
import org.smile.web.scope.ScopeUtils;

/**
 * @author 胡真山
 * 2015年12月1日
 */
public class ScriptTag extends TagSupport implements LoggerHandler {

	protected String var="result";
	
	protected ScriptExecutor executor;
	
	protected String scope;
	
	protected String  script;
	
	protected Object params;
	
	private String type=ScriptType.JS.value();
	
	protected String returnType;
	
	protected boolean view=true;

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public void setVar(String var) {
		this.var = var;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	@Override
	public int doStartTag() throws JspException {
		if(executor==null||!type.equals(executor.getName())){
			executor=new ScriptExecutor(type);
			executor.setResultVar(var);
		}
		Object result=executor.execute(script, params);
		if(StringUtils.notEmpty(returnType)){
			try {
				result=DeclareSupport.convert(returnType, result);
			} catch (Exception e) {
				throw new JspException("convert "+result+" to "+type+" error", e);
			}
		}
		ScopeUtils.setScopeAttribute(var, result, scope, pageContext);
		if(view){
			try {
				pageContext.getOut().print(result);
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		return EVAL_BODY_INCLUDE;
	}

}
