package org.smile.db.function;

import java.util.HashSet;
import java.util.Set;

import org.smile.db.Dialect;

public class FunctionHanlderSupport {
	
	public static String showFunctionSupport(){
		Set<String> supports=new HashSet<String>();
		for(Dialect d:Dialect.values()){
			FunctionHandler h=d.getFunctionHandler();
			if(h!=null){
				supports.addAll(h.viewSupport());
			}
		}
		return supports.toString();
	}
}
