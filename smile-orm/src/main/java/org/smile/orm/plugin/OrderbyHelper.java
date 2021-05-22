package org.smile.orm.plugin;

import java.util.List;

import org.smile.db.criteria.OrderbyCriterion;

/**
 * 排序插件处理
 * @author 胡真山
 *
 */
public class OrderbyHelper {
	
	private static final ThreadLocal<List<OrderbyCriterion>> orderbyParam=new ThreadLocal<List<OrderbyCriterion>>();
	
	/**
	 * 开始排序
	 * @param orderby
	 */
	public static void startOderby(List<OrderbyCriterion> orderby){
		orderbyParam.set(orderby);
	}
	/**
	 * 移除排序属性
	 */
	public static void remove(){
		orderbyParam.remove();
	}
	/**
	 *	 获取排序参数
	 * @return
	 */
	public static List<OrderbyCriterion> getOrderby(){
		return orderbyParam.get();
	}
}
