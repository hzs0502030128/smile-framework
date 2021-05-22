package org.smile.orm.dao;


public class MethodArgs {
	
	private Object[] args;
	
	public MethodArgs(Object[] args){
		this.args=args;
	}
	/**
	 * 是不是空参数
	 * @return
	 */
	public boolean isNullParam(){
		return args==null;
	}
	
	public boolean checkLen(int len){
		if(args==null){
			return false;
		}
		return args.length==len;
	}
	
	public Object getSingleParam(){
		if(args==null){
			return null;
		}
		return args[0];
	}
	
	public Object[] getArgs() {
		return args;
	}
	
	public int getPage(){
		return (Integer)args[args.length-2];
	}
	
	public int getSize(){
		return (Integer)args[args.length-1];
	}
}
