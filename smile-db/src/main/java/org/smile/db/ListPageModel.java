
package org.smile.db;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/**
 * 实现list 接口  - 分页对象  方便分页查询时候统一当成List处理
 * 是一个包含了分页信息的 List 用来替换mybatis查询结果的list
 */
public class ListPageModel<E> extends PageModel<E> implements List<E>{
    /**
     * 不进行count查询
     */
    private static final int NO_SQL_COUNT = -1;
    /**
     * 进行count查询
     */
    private static final int SQL_COUNT = 0;
     /**
     * 分页合理化
     */
    private Boolean reasonable;
    /**
     * 当设置为true的时候，
     * 如果pagesize设置为0（或RowBounds的limit=0），就不执行分页，返回全部结果
     */
    private Boolean pageSizeZero;

    public ListPageModel() {
        super();
    }

    public ListPageModel(int pageNum, int pageSize) {
        this(pageNum, pageSize, SQL_COUNT, null);
    }
    
    public ListPageModel(List<E> data,int page,int pageSize,long totals){
    	super(data, page, pageSize, totals);
    }

    public ListPageModel(int pageNum, int pageSize, boolean count) {
        this(pageNum, pageSize, count ? ListPageModel.SQL_COUNT : ListPageModel.NO_SQL_COUNT, null);
    }

    private ListPageModel(int pageNum, int pageSize, int total, Boolean reasonable) {
        if (pageNum == 1 && pageSize == Integer.MAX_VALUE) {
            pageSizeZero = true;
            pageSize = 0;
        }
        this.page = pageNum;
        this.size = pageSize;
        this.total = total;
        initStartAndEndRow();
        setReasonable(reasonable);
    }

    public void setPage(int pageNum) {
        //分页合理化，针对不合理的页码自动处理
        this.page = ((reasonable != null && reasonable) && pageNum <= 0) ? 1 : pageNum;
    }

    public void setTotal(long total) {
        this.total = total;
        if (size > 0) {
            totalPages = (int) (total / size + ((total % size == 0) ? 0 : 1));
        } else {
        	totalPages = 0;
        }
        //分页合理化，针对不合理的页码自动处理
        if ((reasonable != null && reasonable) && page > totalPages) {
            page = totalPages;
        }
        initStartAndEndRow();
    }

    public void setReasonable(Boolean reasonable) {
        if (reasonable == null) {
            return;
        }
        this.reasonable = reasonable;
        //分页合理化，针对不合理的页码自动处理
        if (this.reasonable && this.page <= 0) {
            this.page = 1;
            initStartAndEndRow();;
        }
    }

    public Boolean getReasonable() {
        return reasonable;
    }

    public Boolean getPageSizeZero() {
        return pageSizeZero;
    }

    public void setPageSizeZero(Boolean pageSizeZero) {
        if (pageSizeZero != null) {
            this.pageSizeZero = pageSizeZero;
        }
    }

    public boolean isCount() {
        return this.total > NO_SQL_COUNT;
    }

    /**
     * 设置合理化
     *
     * @param reasonable
     * @return
     */
    public ListPageModel<E> reasonable(Boolean reasonable) {
        setReasonable(reasonable);
        return this;
    }

    /**
     * 当设置为true的时候，如果pagesize设置为0（或RowBounds的limit=0），就不执行分页，返回全部结果
     *
     * @param pageSizeZero
     * @return
     */
    public ListPageModel<E> pageSizeZero(Boolean pageSizeZero) {
        setPageSizeZero(pageSizeZero);
        return this;
    }

	@Override
	public boolean add(E e) {
		return rows.add(e);
	}

	@Override
	public void add(int index, E element) {
		rows.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return rows.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return rows.addAll(index, c);
	}

	@Override
	public void clear() {
		rows.clear();
	}

	@Override
	public boolean contains(Object o) {
		return rows.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return rows.containsAll(c);
	}

	@Override
	public E get(int index) {
		return rows.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return rows.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return rows.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return rows.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return rows.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return rows.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return rows.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return rows.remove(o);
	}

	@Override
	public E remove(int index) {
		return rows.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return rows.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return rows.retainAll(c);
	}

	@Override
	public E set(int index, E element) {
		return rows.set(index, element);
	}

	@Override
	public int size() {
		return rows.size();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return rows.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return rows.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return rows.toArray(a);
	}
	
}
