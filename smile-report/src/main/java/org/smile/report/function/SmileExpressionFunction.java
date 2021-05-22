package org.smile.report.function;

import org.smile.expression.Context;
import org.smile.expression.Engine;
/**
 * smile 表达式的函数
 * @author 胡真山
 *
 */
public class SmileExpressionFunction extends AbstractFunction{
	//可以在单例下使用此函数
	private static IFunction instance=new SmileExpressionFunction();
	/**表达式引擎*/
	private Engine engine=Engine.getInstance();
	@Override
	public Object convert(Object oneData, String exp, Object expValue) {
		Context context;
		if(oneData instanceof Context){
			context=(Context)oneData;
		}else{
			context=engine.createContext(oneData);
		}
		Object obj=engine.evaluate(context, exp);
		return obj;
	}

	@Override
	public boolean needFieldValue() {
		return false;
	}
	
	
	
	@Override
	public boolean needContext() {
		return true;
	}

	public static IFunction getInstance() {
		return instance;
	}

	public void setEngine(Engine engine){
		this.engine=engine;
	}
}
