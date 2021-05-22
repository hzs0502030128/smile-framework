package org.smile.db.jdbc;

import org.smile.db.criteria.LambdaCriteria;
import org.smile.lambda.Lambda;

import java.util.List;

public interface LambdaRecordDao<E> extends BaseRecordDao<E>{
    /**
     * 只支持lambda方法添加字段的标准
     * @return
     */
    public LambdaCriteria<E> lambda();
    /**
     * 更新一个对象到数据库中
     *
     * @param e 必须指定一个id
     * @param fieldName 属性字段
     */
    public int update(E e, Lambda<E,?>[] fieldName);
    /**
     * 更新多个字段
     * @param e
     * @param first
     * @param others
     * @return
     */
    public int update(E e,Lambda<E,?> first, Lambda<E,?>... others);

    /**
     * 更新一个对象到数据库中
     *
     * @param list 其中的元素必须指定一个id
     * @param fieldName 属性字段
     */
    public int[] updateBatch(List<E> list, Lambda<E,?>[] fieldName);
}
