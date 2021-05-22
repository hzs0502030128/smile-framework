package org.smile.db.callable;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.smile.collection.ArrayUtils;
import org.smile.commons.StringBand;
import org.smile.db.JdbcSupports;
import org.smile.db.Transaction;
import org.smile.db.Transactionable;

public class BaseCallableRunner implements CallableExecutor,Transactionable{
	
	/**用于事务控制*/
	private Transaction transaction;
	
	@Override
	public <T> T callFuction(String functionName,Class<T> returnType,Object... params) throws SQLException{
        StringBand sql=new StringBand("{?=call ");
        sql.append(functionName);
        sql.append("(");
        if(ArrayUtils.notEmpty(params)){
        	int idx=0;
        	for(;idx<params.length;idx++){
        		if(idx>0){
        			sql.append(",");
        		}
        		sql.append("?");
        	}
        }
        sql.append(")}");
        CallableStatement fun=transaction.getConnection().prepareCall(sql.toString()); 
        if(ArrayUtils.notEmpty(params)){
        	int index=2;
        	for(Object obj:params){
        		fun.setObject(index++, obj);
        	}
        }
        fun.registerOutParameter(1, JdbcSupports.getJdbcType(returnType));
        fun.execute(); 
        return  (T)fun.getObject(1);  
	}

	@Override
	public Transaction getTransaction() {
		return transaction;
	}

	@Override
	public void setTransaction(Transaction transaction) {
		this.transaction=transaction;
	}

	@Override
	public Object callFuction(String functionName, Object... params) throws SQLException {
		return callFuction(functionName, null, params);
	}

}
