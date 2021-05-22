package org.smile.report.excel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;



public abstract class Sumer<T>{
	/**
	 * 统计的结果
	 */
	protected Map<String,BigDecimal> sum=new HashMap<String,BigDecimal>();
	/**
	 * 统计一行
	 * @param currentObj
	 */
	public abstract void loop(T currentObj);
	/**
	 * 增加一个统计字段
	 * @param key
	 * @param number
	 */
	public void addSum(String key,BigDecimal number){
		BigDecimal total=sum.get(key);
		if(total==null){
			sum.put(key, number);
		}else if(number!=null){
			total=total.add(number);
			sum.put(key, total);
		}
	}
	/**
	 * 把汇总信息设置到填充内容中
	 * @param context
	 */
	protected  void  setToContext(Map<String,Object> context){
		context.putAll(sum);
	}
	
	public void clear(){
		sum.clear();
	}
	
}
