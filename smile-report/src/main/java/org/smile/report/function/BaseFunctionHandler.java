package org.smile.report.function;

import java.util.HashMap;
import java.util.Map;

import org.smile.function.BasicFunctionParser;
import org.smile.function.BaseFunctionInfo;
import org.smile.function.FunctionParser;
import org.smile.log.LoggerHandler;

public class BaseFunctionHandler implements FunctionHandler,LoggerHandler {
	/** 注册的函数 */
	protected Map<String,IFunction> functions=new HashMap<String, IFunction>();
	/**对表达式进行解析*/
	protected FunctionParser parser=new BasicFunctionParser('{','}'){
		@Override
		protected BaseFunctionInfo createFunctionExpInfo(String name, String exp) {
			return new BaseFunctionInfo(name,new String[]{exp});
		}
	};
	
	public BaseFunctionHandler(){
		try{
			registerFunction("$",SmileExpressionFunction.getInstance());
			registerFunction("template",TemplateFunction.instance);
			registerFunction("fn",BeanPropertyFunction.instance);
			registerFunction("ognl",OgnlSupport.getOgnlFunction());
		}catch(Exception e){
			logger.info("没有对ognl支持,如需要支持请引入正确的ongl支持jar");
		}
	}
	
	@Override
	public IFunction getFunction(BaseFunctionInfo fun) {
		return functions.get(fun.getName());
	}

	@Override
	public BaseFunctionInfo getFunctionInfo(String exp){
		return parser.parse(exp);
	}

	@Override
	public <E extends IFunction> void registerFunction(String name, E function) {
		functions.put(name, function);
	}

	@Override
	public boolean isFunction(BaseFunctionInfo fun) {
		if(fun==null){
			return false;
		}
		return functions.containsKey(fun.getName());
	}

}
