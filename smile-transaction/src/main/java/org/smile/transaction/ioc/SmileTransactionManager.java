package org.smile.transaction.ioc;

import java.util.Map;

import org.smile.beans.converter.BeanException;
import org.smile.collection.MapUtils;
import org.smile.ioc.BeanFactory;
import org.smile.ioc.load.FactoryBeanRegistry;
import org.smile.transaction.AnnotationTransactionInterceptor;
import org.smile.transaction.RegExpTransactionInterceptor;
import org.smile.transaction.TransactionType;
/***
 * 事务管理 
 * @author 胡真山
 * 2015年9月3日
 */
public class SmileTransactionManager implements FactoryBeanRegistry{
	/**使用的方法默认使用代理的方式*/
	private TransactionType type=TransactionType.proxy;
	/**管理的类*/
	private String classRegString;
	/**管理的方法*/
	private Map<String,String> methodReg;
	
	public void setType(String type) {
		this.type = TransactionType.of(type);
	}
	@Override
	public void processBeanRegistry(BeanFactory beanFactory) throws BeanException {
		BeanFactory factory=beanFactory;
		if(type==TransactionType.proxy){
			if(MapUtils.notEmpty(methodReg)) {
				factory.registInterceptor(new RegExpTransactionInterceptor(classRegString, methodReg));
			}else {
				factory.registInterceptor(new AnnotationTransactionInterceptor());
			}
			
		}
	}
	
	public void setMethodReg(Map<String, String> methodReg) {
		this.methodReg = methodReg;
	}
	
	public void setClassRegString(String classRegString) {
		this.classRegString = classRegString;
	}
	
	
}
