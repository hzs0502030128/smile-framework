package org.smile.expression.script;

import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.smile.expression.Context;
import org.smile.expression.DefaultContext;
import org.smile.expression.Engine;
import org.smile.script.ContextBindings;


public class SmileScriptEngine extends AbstractScriptEngine{
	/**创建此引擎的工厂*/
	private ScriptEngineFactory factory;
	
	/**表达式引擎*/
	private Engine engine=Engine.getInstance();
	
	protected SmileScriptEngine(ScriptEngineFactory factory) {
		this.factory=factory;
	}

	@Override
	public ScriptEngineFactory getFactory() {
		return factory;
	}

	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException {
		Context cxt=new ContextBindings(context);
		return engine.evaluate(cxt, script);
	}


	@Override
	public Object eval(Reader reader, ScriptContext context) throws ScriptException {
		Context cxt=new ContextBindings(context);
		return engine.evaluate(cxt, reader.toString());
	}

	@Override
	public Object eval(Reader reader, Bindings bindings) throws ScriptException {
		return eval(reader.toString(), bindings);
	}



	@Override
	public Object eval(String script, Bindings bindings) throws ScriptException {
		if(bindings instanceof ContextBindings) {
			return engine.evaluate((Context)bindings,script);
		}
		return super.eval(script, bindings);
	}



	@Override
	public Object eval(String script) throws ScriptException {
		return engine.evaluate(new ContextBindings(context), script);
	}

	@Override
	public Bindings createBindings() {
		return new ContextBindings(context);
	}
	

	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	

}
