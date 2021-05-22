package org.smile.db.jdbc;

import org.smile.db.criteria.Criteria;

import java.util.Collection;
import java.util.List;

public interface BaseRecordDao<E> {
    /**
     * 查询一个表的所有数据
     * @return
     */
    public List<E> queryAll();
    /**
     * 以多个id查询数据
     *
     * @param id id的数组
     * @return 查询出的数据封闭成的对象的列表
     */
    public  List<E> queryByIds(Object... id);

    /**
     * 以id查询一条数据
     * @param id 要查询数据的主键
     * @return 查询出的一个对象
     */
    public  E queryById(Object id);

    /**
     * 以id删除
     *
     * @param id
     */
    public int deleteById(Object id);
    /***
     * 按id批量删除
     * @param ids
     * @return
     */
    public int[] deleteByIds(Collection ids);

    /**
     * 删除所有数据
     *
     */
    public int deleteAll();

    /**
     * 添加一条数据
     *
     * @param obj
     */
    public void add(E obj);

    /**
     * 添加多条为数据
     *
     * @param c
     */
    public void add(List<E> c);

    /**
     * 批量按id删除
     * @param ids
     * @return
     */
    public int[] deleteByIds(Object[] ids);

    /**
     * 插入或保存  以主键判断
     * @param obj 要处理的对象
     */
    public void saveOrUpdate(Object obj);

    /**
     * 创建一个Criteria实例
     * @return
     */
    public Criteria<E> criteria();
}
