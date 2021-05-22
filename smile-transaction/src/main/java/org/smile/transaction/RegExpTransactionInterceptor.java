package org.smile.transaction;

import java.util.Map;

import org.smile.plugin.ClassRegExpInterceptor;
import org.smile.plugin.Invocation;
import org.smile.plugin.MethodInterceptor;
import org.smile.plugin.Plugin;
import org.smile.util.RegExp;

public class RegExpTransactionInterceptor extends ClassRegExpInterceptor{
	/**需要代理的类名称配置正则表达式*/
	private RegExp classReg;
	
	protected RegExpTransactionInterceptor() {}
	
	public RegExpTransactionInterceptor(String classString,Map<String,String> methodReg){
		this.classReg=Plugin.createMethodReg(classString);
		for(Map.Entry<String, String> entry:methodReg.entrySet()){
			Propagation propagation=Propagation.valueOf(entry.getValue());
			if(Propagation.REQUIRED==propagation) {
				registMethodInterceptor(entry.getKey(), new ProxyTransactionMethod());
			}else if(Propagation.SUPPORT==propagation) {
				registMethodInterceptor(entry.getKey(), new ProxyNoTransactionMethod());
			}
		}
	}

	@Override
	protected boolean needProxy(Class targetClass) {
		return classReg.matches(targetClass.getName());
	}
	
	class ProxyNoTransactionMethod implements MethodInterceptor {

		@Override
		public Object intercept(Invocation invocation) throws Exception {
			try{
				TransactionUtils.startManagered(false);
				Object result= invocation.proceed();
				return result;
			}catch(Throwable e){
				throw new TransactionException(e);
			}finally{
				TransactionUtils.endManagered();
				TransactionUtils.closeAllTransactions();
			}
		}

	}
	
	class ProxyTransactionMethod implements MethodInterceptor {

		@Override
		public Object intercept(Invocation invocation) throws Exception {
			try{
				TransactionUtils.startManagered(true);
				Object result= invocation.proceed();
				TransactionUtils.commitAllTransactions();
				return result;
			}catch(Throwable e){
				TransactionUtils.rollBackAllTransactions();
				throw new TransactionException(e);
			}finally{
				TransactionUtils.endManagered();
				TransactionUtils.closeAllTransactions();
			}
		}

	}

	
	
}
