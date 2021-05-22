package org.smile.transaction;

import java.util.Stack;
/**
 * 	管理事务开启状态
 * @author 胡真山
 * @Date 2016年1月12日
 */
public class TransactionState {
	
	private  ThreadLocal<Stack<Boolean>> isSynTransaction=new ThreadLocal<Stack<Boolean>>();
	/**
	 * 当前事务开启状态进栈
	 */
	public  void synTransaction(boolean needCommit){
		Stack<Boolean> stack=isSynTransaction.get();
		if(stack==null){
			stack=new Stack<Boolean>();
			isSynTransaction.set(stack);
		}
		stack.push(needCommit);
	}
	/**
	 * 当前线程事务开启状态出栈
	 */
	public  void pop(){
		Stack<Boolean> stack=isSynTransaction.get();
		if(stack!=null){
			stack.pop();
		}
	}
	
	public Boolean peek(){
		Stack<Boolean> stack=isSynTransaction.get();
		if(stack!=null){
			if(stack.size()>0) {
				return stack.peek(); 
			}
		}
		return null;
	}
	/**
	 * 移除当前线程的状态
	 */
	public void clearTransactionStatus(){
		isSynTransaction.remove();
	}
	/***
	 * 是否已开启事务管理
	 * @return
	 */
	public boolean isBeginTransaction(){
		Stack<Boolean> stack=isSynTransaction.get();
		if(stack==null){
			return false;
		}else{
			Boolean res=peek();
			return res==Boolean.TRUE;
		}
	}
	/**
	 * 是否已经同步事务
	 * @return
	 */
	public boolean isSynTransaction() {
		Stack<Boolean> stack=isSynTransaction.get();
		if(stack==null){
			return false;
		}else {
			Boolean res=peek();
			return res!=null;
		}
	}
}
