package org.smile.db;


/**
 * 分页查询插件
 * @author 胡真山
 *
 */
public class PageHelper {
	/**保存当前线程的分页信息*/
	private static final ThreadLocal<PageParam> pageParam=new ThreadLocal<PageParam>();
	/**
	 * 设置分参数
	 * @param page
	 * @param size
	 */
	public static void startPage(int page,int size){
		pageParam.set(new PageParam(page, size));
	}
	/**
	 * 开始分页
	 * @param param 分页参数设置
	 */
	public static void startPage(PageParam param){
		pageParam.set(param);
	}
	/**
	 * 移除分页参数
	 */
	public static void remove(){
		pageParam.remove();
	}
	/**
	 * 获取分页参数
	 * @return
	 */
	public static PageParam getPageParam(){
		return pageParam.get();
	}
}
