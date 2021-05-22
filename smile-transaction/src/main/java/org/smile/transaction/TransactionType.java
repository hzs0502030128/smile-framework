package org.smile.transaction;
/***
 * 事务管理实现方式
 * @author 胡真山
 * 2015年9月3日
 */
public enum TransactionType {
	/**代理方式实现*/
	proxy,
	/**aop方式*/
	aop;
	
	public static TransactionType of(String type){
		return TransactionType.valueOf(type);
	}
}
