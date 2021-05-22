package org.smile.template;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.smile.beans.PropertyHandler;
import org.smile.beans.converter.BeanException;
import org.smile.beans.handler.MapPropertyHandler;
import org.smile.collection.AbstractMapDecorator;
import org.smile.commons.SmileRunException;
import org.smile.function.BasicFunctionParser;
import org.smile.function.Function;
import org.smile.function.BaseFunctionInfo;
import org.smile.function.FunctionParser;
import org.smile.function.FunctionWrap;
import org.smile.util.StringUtils;

public class FunctionWrapMap extends AbstractMapDecorator<String, Object> implements FunctionWrap{
	
	private static FunctionParser parser=new BasicFunctionParser('(',')');
	/** 函数信息*/
	private Map<String, Function> functionMap;
	
	private PropertyHandler handler=MapPropertyHandler.DEFAULT;
	
	public FunctionWrapMap(){
		super(new HashMap<String, Object>());
		functionMap=new HashMap<String, Function>();
	}
	
	public FunctionWrapMap(Map realMap){
		super(realMap);
		functionMap=new HashMap<String, Function>();
	}
	
	public FunctionWrapMap(Map realMap,Map<String, Function> functionMap){
		super(realMap);
		this.functionMap=functionMap;
	}

	
	@Override
	public Object get(Object key) {
		if(key instanceof String){
			String exp=((String) key);
			BaseFunctionInfo info=parser.parse(exp);
			if(info!=null){
				Function f=functionMap.get(info.getName());
				if(f!=null){
					return f.getFunctionValue(getArgsValue(info));
				}
			}
		}
		return realMap.get(key);
	}
	
	private Object[] getArgsValue(BaseFunctionInfo expInfo){
		String[] args=expInfo.getArgExpression();
		Object[] result=new Object[args.length];
		for(int i=0;i<args.length;i++){
			String arg=StringUtils.trim(args[i]);
			if(realMap.containsKey(arg)){
				try {
					result[i]=handler.getExpFieldValue(realMap,arg);
				} catch (BeanException e) {
					throw new SmileRunException(e);
				}
			}else{
				char[] argchars=arg.toCharArray();
				if(argchars.length>2){
					if(argchars[0]=='\'' &&argchars[argchars.length-1]=='\''){
						result[i]=arg.substring(1, argchars.length-1);
					}else if(argchars[0]=='\"' &&argchars[argchars.length-1]=='\"'){
						result[i]=arg.substring(1, argchars.length-1);
					}else{
						result[i]=arg;
					}
				}else{
					result[i]=arg;
				}
			}
		}
		return result;
	}

	@Override
	public void registFunction(Function f) {
		functionMap.put(f.getName(), f);
	}

	public void setFunctionMap(Map<String, Function> functionMap) {
		this.functionMap = functionMap;
	}

	@Override
	public void registFunctions(Collection<Function> fs) {
		for(Function f:fs){
			registFunction(f);
		}
	}
	
}
