package org.smile.expression;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.collection.SoftHashMap;
import org.smile.commons.Strings;
import org.smile.expression.parser.ExpressionCCParser;
import org.smile.expression.parser.ParseException;
import org.smile.function.Function;
import org.smile.log.LoggerHandler;
import org.smile.tag.config.TagLibContext;
import org.smile.util.StringUtils;
/**表达式引擎*/
public class Engine implements ExpressionEngine, LoggerHandler{
	/**默认函数库,新建引擎时都会添加此函数库*/
	private static final Map<FunctionKey,Function> DEFAULT_FUNCTIONS=new ConcurrentHashMap<FunctionKey,Function>();
	/**一个默认的引擎实例,一般情况使用此实现即可*/
	private static Engine instance;
	/**注册函数集*/
	private Map<FunctionKey,Function> functions=new ConcurrentHashMap<FunctionKey,Function>();
	/**对解析出的表达式进行缓存*/
	protected Map<String,Expression> cacheExpression= SoftHashMap.newConcurrentInstance();
	
	protected boolean executeCode=true;
	
	static{
		//注册默认函数库
		TagLibContext.getInstance().registFunction(DEFAULT_FUNCTIONS, Strings.BLANK, TagLibContext.DEFAULT_FUNCTIONS);
	}
	
	public Engine(){
		//添加默认的函数库
		functions.putAll(DEFAULT_FUNCTIONS);
	}
	
	/**
	 * 是否可以执行代码
	 * @return
	 */
	public boolean isExecuteCode() {
		return executeCode;
	}

	/**
	 * 设置是否可以执行代码
	 * @param executeCode
	 */
	public void setExecuteCode(boolean executeCode) {
		this.executeCode = executeCode;
	}



	/**
	 * 解析表达式
	 * @param exp
	 * @return
	 */
	public <T> Expression<T> parseExpression(String exp){
		Expression<T> expression=cacheExpression.get(exp);
		if(expression==null){
			try {
				expression=ExpressionCCParser.parse(exp);
				cacheExpression.put(exp, expression);
			} catch (ParseException e) {
				throw new EvaluateException("解析异常-->"+exp, e);
			}
		}
		return expression;
	}
	
	/***
	 * 获取默认的引擎实例
	 * @return
	 */
	public synchronized static Engine getInstance() {
		if(instance==null){
			instance=new Engine();
		}
		return instance;
	}
	
	/**
	 * 向默认库中添加一个函数
	 * @param f
	 */
	public static void defaultFunction(String prefix,Function f) {
		String name=StringUtils.isEmpty(prefix)?f.getName():prefix+":"+f.getName();
		DEFAULT_FUNCTIONS.put(new FunctionKey(name,f.getSupportArgsCount()), f);
	}
	/***
	 * 解析一个表达式
	 * @param rootValue
	 * @param expression
	 * @return
	 */
	@Override
	public Object evaluate(Context cxt,String expression){
		Expression c=parseExpression(expression);
		return c.evaluate(cxt);
	}
	/**
	 * 注册自定义函数
	 * @param f
	 */
	public void registFunction(String prefix, Function f){
		String name=StringUtils.isEmpty(prefix)?f.getName():prefix+":"+f.getName();
		this.functions.put(new FunctionKey(name,f.getSupportArgsCount()), f);
	}
	
	/**
	 * 注册自定义函数库
	 * @param prefix
	 * @param libName
	 */
	public void registFunctionLib(String prefix,String libName){
		TagLibContext.getInstance().registFunction(functions, prefix, libName);
	}
	
	/**
	 * 获取函数
	 * @param key
	 * @return
	 */
	public Function getFunction(FunctionKey key){
		return this.functions.get(key);
	}
	/**
	 * 创建一个上下文 可使用此引擎的函数库
	 * @param param
	 * @return
	 */
	@Override
	public Context createContext(Object param){
		DefaultContext context=new DefaultContext(param);
		context.setEngine(this);
		return context;
	}
	
	public static class FunctionKey{
		
		String name;
		
		int argsCount=-1;
		
		public FunctionKey(String name){
			this.name=name;
		}
		
		public FunctionKey(){
			
		}
		
		public void setName(String name) {
			this.name = name;
		}

		public FunctionKey(String name,int argsCount){
			this.name=name;
			this.argsCount=argsCount;
		}
		
		public void setArgsCount(int argsCount) {
			this.argsCount = argsCount;
		}
		
		public String getName() {
			return name;
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj==null){
				return false;
			}
			if(obj instanceof FunctionKey){
				FunctionKey other=(FunctionKey)obj;
				return name.equals(other.name)&&(argsCount==other.argsCount||other.argsCount==-1);
			}
			return false;
		}
	}
}
