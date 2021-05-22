package org.smile.tag;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.expression.Context;
import org.smile.expression.Engine;
import org.smile.expression.ExpressionEngine;
import org.smile.tag.parser.ParseResult;
import org.smile.tag.parser.TagTokenizer;

public class TagEngine{
	
	public static TagEngine DEFAULT=new TagEngine();
	
	
	
	private State state=State.getDefault();

	/**
	 * 解析结果缓存
	 */
	private Map<CharSequence,ParseResult> cachedParseResults=new ConcurrentHashMap<CharSequence, ParseResult>();
	/**
	 * 表达式引擎
	 */
	protected ExpressionEngine expressionEngine;
	
	public TagEngine(ExpressionEngine expressionEngine){
		this.expressionEngine=expressionEngine;
	}
	
	public TagEngine(){
		this.expressionEngine=new Engine();
	}
	
	/**
	 * 创建一个context
	 * @param root
	 * @return
	 */
	public TagContext createTagContext(Object root) {
		return new SimpleContext(state,root);
	}
	
	/**
	 * 解析模板
	 * @param state
	 * @param template
	 * @return
	 */
	public ParseResult parse(State state,CharSequence template){
		ParseResult context=this.cachedParseResults.get(template);
		if(context!=null){
			return context;
		}
		context=new ParseResult(state);
		parse(template, context);
		this.cachedParseResults.put(template, context);
		return context;
	}
    
    public void parse(CharBuffer in,final ParseResult parsed){
        TagTokenizer tokenizer = new TagTokenizer(in,parsed );
        tokenizer.start();
    }
    
    public void parse(CharSequence in,final ParseResult parsed){
        TagTokenizer tokenizer = new TagTokenizer(in,parsed );
        tokenizer.start();
    }
    /**
     *	 
     * @param tagContext
     * @param parsed
     * @throws Exception
     */
    public void evaluate(TagContext tagContext,ParseResult parsed) throws Exception{
    	List<Fragment> list=parsed.getResults();
    	tagContext.setTagEngine(this);
    	for(Fragment f:list){
    		f.invoke(tagContext);
    	}
    	tagContext.getWriter().flush();
    }
    /**
     *	 解析模板
     * @param tagContext
     * @param template
     * @throws Exception
     */
    public void evaluate(TagContext tagContext,CharSequence template) throws Exception{
    	ParseResult result=parse(tagContext.currentState(),template);
    	evaluate(tagContext, result);
    }
    
    /**
     * 	设置表达式引擎
     * @param engine
     */
    public void setExpressionEngine(ExpressionEngine engine){
    	this.expressionEngine=engine;
    }
    /**
     * 	当前使用的表达式引擎
     * @return
     */
    public ExpressionEngine expressionEngine(){
    	return this.expressionEngine;
    }
    /**
     * 	解析表达式
     * @param context
     * @param expression
     * @return
     */
    public Object evaluteExpression(Context context,String expression){
    	return this.expressionEngine.evaluate(context, expression);
    }

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
    
    
}

