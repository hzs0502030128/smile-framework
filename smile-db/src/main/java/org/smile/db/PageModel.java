package org.smile.db;


import java.io.Serializable;
import java.util.List;

import org.smile.commons.PagerResultSupport;
import org.smile.commons.PagerSupport;
import org.smile.json.JSON;
/**
 * 分页数据
 * @author strive
 *
 */
public class PageModel<E> implements PagerSupport,PagerResultSupport,Serializable{
	/**
	 * 数据
	 */
	protected List<E> rows;
	/**
	 * 总条娄
	 */
	protected long total;
	/**
	 * 一页条数
	 */
	protected int size;
	/**
	 * 当前页
	 */
	protected int page;
	/**
	 * 总页数
	 */
	protected int totalPages;
	/**
     * 起始行
     */
    protected long startRow;
    /**
     * 末行
     */
    protected long endRow;
	
    protected int offset;
    
	public PageModel(){}
	
	public PageModel(List<E> list){
		this.rows=list;
		this.page=1;
		this.total=list.size();
		this.totalPages=page;
	}
	
	public List<E> getRows() {
		return this.rows;
	}
	public void setRows(List<E> rows) {
		this.rows = rows;
	}
	public long getTotal() {
		return this.total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	@Override
	public int getSize() {
		return this.size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	@Override
	public int getPage() {
		return this.page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public long getTotalPages() {
		return this.totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	/***
	 * 初始化数据
	 * @param data 结果数据
	 * @param page 当前页
	 * @param pageSize 每页长度
	 * @param totals 总条数
	 */
	public void initData(List<E> data,int page,int pageSize,long totals){
		this.rows=data;
		this.page=page;
		this.size=pageSize;
		this.offset=(page-1)*size;
		this.total=totals;
		initStartAndEndRow();
		this.totalPages=(int)(totals%pageSize==0?totals/pageSize:totals/pageSize+1);
	}
	
	public PageModel(List<E> data,int page,int pageSize,long totals){
		initData(data, page, pageSize, totals);
	}
	@Override
	public int getOffset(){
		return (page-1)*size;
	}
	
	/**
     * 计算起止行号
     */
    protected void initStartAndEndRow() {
    	this.startRow=size*(page-1)+1;
		this.endRow=(page*size)>total?total:page*size;
    }
	/**
	 * 重写toString
	 */
    @Override
	public String toString(){
		return JSON.toJSONString(this);
	}

	public long getStartRow() {
		return this.startRow;
	}

	public long getEndRow() {
		return this.endRow;
	}
	
	public PageModel clone(){
		PageModel pm=new PageModel();
		pm.setRows(rows);
		pm.setPage(page);
		pm.setSize(size);
		pm.setTotal(total);
		pm.setTotalPages(totalPages);
		pm.initStartAndEndRow();
		return pm;
	}

	public ListPageModel toListPageModel(){
		ListPageModel pm=new ListPageModel();
		pm.setRows(rows);
		pm.setPage(page);
		pm.setSize(size);
		pm.setTotal(total);
		pm.setTotalPages(totalPages);
		pm.initStartAndEndRow();
		return pm;
	}

	@Override
	public long getTotalRows() {
		return total;
	}
	/**
	 * 总记录条数
	 * @return
	 */
	public long getCount(){
		return total;
	}
}

