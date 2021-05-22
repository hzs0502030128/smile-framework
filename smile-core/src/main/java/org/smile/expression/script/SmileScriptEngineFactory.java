package org.smile.expression.script;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import org.smile.json.JSONValue;

/**
 * 
 * Smile Expression Extends Language Engine
 * 
 *     表达式扩展语言引擎
 * @author 胡真山
 *
 */
public class SmileScriptEngineFactory implements ScriptEngineFactory {

	/** {@inheritDoc} */
	public String getEngineName() {
		return "SMILE ELXL Engine";
	}

	/** {@inheritDoc} */
	public String getEngineVersion() {
		return "1.0";
	}

	/** {@inheritDoc} */
	public String getLanguageName() {
		return "smile_elxl";
	}

	/** {@inheritDoc} */
	public String getLanguageVersion() {
		return "1.0";
	}

	/** {@inheritDoc} */
	public String getMethodCallSyntax(String obj, String m, String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append(obj);
		sb.append('.');
		sb.append(m);
		sb.append('(');
		boolean needComma = false;
		for (String arg : args) {
			if (needComma) {
				sb.append(',');
			}
			sb.append(arg);
			needComma = true;
		}
		sb.append(')');
		return sb.toString();
	}

	/** {@inheritDoc} */
	public List<String> getExtensions() {
		return Collections.unmodifiableList(Arrays.asList("selxl", "SELXL"));
	}

	/** {@inheritDoc} */
	public List<String> getMimeTypes() {
		return Collections.unmodifiableList(Arrays.asList("application/x-selxl", "application/x-selxl"));
	}

	/** {@inheritDoc} */
	public List<String> getNames() {
		return Collections.unmodifiableList(Arrays.asList("smile_elxl", "SMILE_ELXL"));
	}

	/** {@inheritDoc} */
	public String getOutputStatement(String toDisplay) {
		if (toDisplay == null) {
			return "System.out.print(null)";
		} else {
			return "System.out.print(" + JSONValue.toJSONString(toDisplay) + ")";
		}
	}

	/** {@inheritDoc} */
	public Object getParameter(String key) {
		if (key.equals(ScriptEngine.ENGINE)) {
			return getEngineName();
		} else if (key.equals(ScriptEngine.ENGINE_VERSION)) {
			return getEngineVersion();
		} else if (key.equals(ScriptEngine.NAME)) {
			return getNames();
		} else if (key.equals(ScriptEngine.LANGUAGE)) {
			return getLanguageName();
		} else if (key.equals(ScriptEngine.LANGUAGE_VERSION)) {
			return getLanguageVersion();
		} else if (key.equals("THREADING")) {
			/*
			 * To implement multithreading, the scripting engine context
			 * (inherited from AbstractScriptEngine) would need to be made
			 * thread-safe; so would the setContext/getContext methods. It is
			 * easier to share the underlying Uberspect and JEXL engine
			 * instance, especially with an expression cache.
			 */
			return null;
		}
		return null;
	}

	/** {@inheritDoc} */
	public ScriptEngine getScriptEngine() {
		SmileScriptEngine engine = new SmileScriptEngine(this);
		return engine;
	}

	@Override
	public String getProgram(String... statements) {
		StringBuilder $retval = new StringBuilder("<?\n");
		int len = statements.length;
		for (int i = 0; i < len; i++) {
			$retval.append(statements[i] + ";\n");
		}
		$retval.append("?>");
		return $retval.toString();
	}
}
