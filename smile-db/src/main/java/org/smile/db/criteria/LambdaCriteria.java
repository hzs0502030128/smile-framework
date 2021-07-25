package org.smile.db.criteria;

import org.smile.lambda.Lambda;

public interface LambdaCriteria<E> extends BaseCriteria<E>{
    /**
     * 新增加一个betwen查询条件 and连接
     * @param fieldName
     * @param start
     * @param end
     * @return
     */
    public LambdaCriteria<E> between(Lambda<E,?> fieldName, Object start, Object end);

    /**
     * 相等条件
     * @param fieldName
     * @param value
     * @return
     */
    public LambdaCriteria<E> eq(Lambda<E,?> fieldName,Object value);

    /**
     * 为空的条件
     * @param fieldName
     * @return
     */
    public LambdaCriteria<E> isnull(Lambda<E,?> fieldName);

    /**
     * 不为空条件
     * @param fieldName
     * @return
     */
    public LambdaCriteria<E> notnull(Lambda<E,?> fieldName);

    /**
     * 不相等
     * @param fieldName
     * @param value
     * @return
     */
    public LambdaCriteria<E> ne(Lambda<E,?> fieldName,Object value);

    /**
     * 小于
     * @param fieldName
     * @param value
     * @return
     */
    public LambdaCriteria<E> lt(Lambda<E,?> fieldName,Object value);

    /**
     * 大于
     * @param fieldName
     * @param value
     * @return
     */
    public LambdaCriteria<E> gt(Lambda<E,?> fieldName,Object value);

    /**
     * 小于等于
     * @param fieldName
     * @param value
     * @return
     */
    public LambdaCriteria<E> le(Lambda<E,?> fieldName,Object value);

    /**
     * 大于等于
     * @param fieldName
     * @param value
     * @return
     */
    public LambdaCriteria<E> ge(Lambda<E,?> fieldName,Object value);

    /**
     * IN 查询条件
     * @param fieldName
     * @param value
     * @return
     */
    public LambdaCriteria<E> in(Lambda<E,?> fieldName,Object value);

    /**
     * not in
     * @param fieldName
     * @param value
     * @return
     */
    public LambdaCriteria<E> nin(Lambda<E,?> fieldName,Object value);

    /**
     * 模糊查询
     * @param fieldName
     * @param value
     * @param mathMode
     * @return
     */
    public LambdaCriteria<E> like(Lambda<E,?> fieldName,String value,MatchMode mathMode);

    /**
     * 模糊查询
     * @param fieldName
     * @param value
     * @return
     */
    public LambdaCriteria<E> like(Lambda<E,?> fieldName,String value);
    public LambdaCriteria<E> nlike(Lambda<E,?> fieldName,String value);
    public LambdaCriteria<E> nlike(Lambda<E,?> fieldName,String value,MatchMode mathMode);
    public LambdaCriteria<E> eqother(Lambda<E,?> fieldName,Lambda<E,?> otherField);
    public LambdaCriteria<E> neother(Lambda<E,?> fieldName,Lambda<E,?> otherField);
    public LambdaCriteria<E> ltother(Lambda<E,?> fieldName,Lambda<E,?> otherField);
    public LambdaCriteria<E> gtother(Lambda<E,?> fieldName,Lambda<E,?> otherField);
    public LambdaCriteria<E> leother(Lambda<E,?> fieldName,Lambda<E,?> otherField);
    public LambdaCriteria<E> geother(Lambda<E,?> fieldName,Lambda<E,?> otherField);
    /**
     * 设置排序字段
     * @param fieldName
     * @return
     */
    public LambdaCriteria<E> orderby(Lambda<E,?> fieldName);

    /**
     * 设置一个排序字段
     * @param fieldName
     * @param desc
     * @return
     */
    public LambdaCriteria<E> orderby(Lambda<E,?> fieldName,boolean desc);

    /**
     * 设置查询字段
     * @param fieldName
     * @return
     */
    public LambdaCriteria<E> field(Lambda<E,?> fieldName);

    /**
     * 设置更新字段
     * @param fieldName
     * @return
     */
    public LambdaCriteria<E> set(Lambda<E,?> fieldName,Object value);


    /**
     * 	设置查询字段
     * @param first
     * @param fieldNames
     * @return
     */
    public LambdaCriteria<E> fields(Lambda<E,?> first,Lambda<E,?>...fieldNames);
}
