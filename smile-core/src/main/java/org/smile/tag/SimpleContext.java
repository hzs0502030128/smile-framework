package org.smile.tag;

import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.smile.collection.ThreadLocalMap;
import org.smile.expression.DefaultContext;
import org.smile.io.CharArrayWriter;

public class SimpleContext extends DefaultContext implements TagContext {
	/**当前的状态*/
	private State currentState;
	/**缓存处理的内容*/
	private Writer writer;
	/**当前的解析器*/
	private TagEngine tagEngine;
	/**设置属性*/
	private Map<String,Object> attributes=new ThreadLocalMap<String,Object>();
	
	public SimpleContext(Object root) {
	   super(root);
       this.currentState=State.getDefault();
       this.writer=new CharArrayWriter();
    }
	
	public SimpleContext() {
       this.currentState=State.getDefault();
       this.writer=new CharArrayWriter();
    }
	
	public SimpleContext(State state,Object root) {
		super(root);
		this.currentState=state;
		this.writer=new CharArrayWriter();
	}
	
	public SimpleContext(State state){
		this.currentState=state;
	}

    public State currentState() {
        return currentState;
    }

    public void changeState(State newState) {
        currentState = newState;
    }

	
	@Override
	public void setTagEngine(TagEngine engin) {
		tagEngine=engin;
	}
	/**
	 * 解析表达式
	 * @param expression
	 * @return
	 */
	public Object evaluate(String expression){
		return tagEngine.evaluteExpression(this, expression);
	}
	
	public void setAttribute(String name,Object value){
		attributes.put(name, value);
	}
	
	public void setAttribute(String name,Object value,Scope scope){
		if(scope==Scope.page){
			attributes.put(name, value);
		}else if(scope==Scope.context){
			set(name, value);
		}
	}
	
	public Object getAttribute(String name,Scope scope){
		if(scope==Scope.page){
			return attributes.get(name);
		}else{
			return get(name);
		}
	}
	
	public Object getAttribute(String name){
		Object obj= attributes.get(name);
		if(obj!=null){
			return obj;
		}
		return get(name);
	}
	
	@Override
	public Object get(String exp) {
		Object value=getFieldValue(attributes,exp);
		if(value==null){
			return super.get(exp);
		}
		return value;
	}

	public void removeAttribute(String name,Scope scope){
		if(scope==Scope.page){
			attributes.remove(name);
		}else{
			remove(name);
		}
	}
	/**
	 * 清空设置的属性值
	 */
	public void clearAttributes() {
		this.attributes.clear();
	}
	
	public void removeAttribute(String name){
		attributes.remove(name);
	}

	public Locale getLocale() {
		return null;
	}

	@Override
	public TagEngine getTagEngine() {
		return this.tagEngine;
	}

	@Override
	public Writer getWriter() {
		return writer;
	}
	
	@Override
	public void setWriter(Writer writer){
		this.writer=writer;
	}

}
