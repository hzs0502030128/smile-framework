package org.smile.db;

import org.smile.Smile;

/**
 * 分页参数
 * @author 胡真山
 *
 */
public class PageParam{
	/**
	 * 默认每页条数
	 */
	public static int DEFAULT_PAGE_SIZE;
	
	static{
		DEFAULT_PAGE_SIZE=Smile.config.getValue(Smile.PAGER_PAGE_SIZE_KEY, int.class, 20);
	}
	/**
	 * 当前页
	 */
	protected int page;
	/**每一页条数*/
	protected int pageSize;
	/**自定义count方法*/
	protected String countMethod;
	/**是否按添加Count扩展为预设count方法名*/
	protected boolean countExtension;
	
	public PageParam(){
		this.page=1;
		this.pageSize=DEFAULT_PAGE_SIZE;
	}
	
	public PageParam(int page,int pageSize){
		this.page=page;
		this.pageSize=pageSize;
	}
	/**
	 * 偏移行数
	 * @return
	 */
	public int getOffset(){
		return (page-1)*pageSize;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getCountMethod() {
		return countMethod;
	}

	public PageParam setCountMethod(String countMethod) {
		this.countMethod = countMethod;
		return this;
	}

	public boolean isCountExtension() {
		return countExtension;
	}

	public PageParam setCountExtension(boolean countExtension) {
		this.countExtension = countExtension;
		return this;
	}
	
}
