package org.smile.db.criteria;

public interface Criteria<E> extends LambdaCriteria<E>{
    /**
     * 新增加一个betwen查询条件 and连接
     * @param fieldName
     * @param start
     * @param end
     * @return
     */
    public Criteria<E> between(String fieldName, Object start, Object end);
    public Criteria<E> eq(String fieldName, Object value);
    public Criteria<E> isnull(String fieldName);
    public Criteria<E> notnull(String fieldName);
    public Criteria<E> ne(String fieldName, Object value);
    public Criteria<E> lt(String fieldName, Object value);
    public Criteria<E> gt(String fieldName, Object value);
    public Criteria<E> le(String fieldName, Object value);
    public Criteria<E> ge(String fieldName, Object value);
    public Criteria<E> in(String fieldName, Object value);
    public Criteria<E> like(String fieldName, String value, MatchMode mathMode);
    public Criteria<E> like(String fieldName, String value);
    public Criteria<E> nlike(String fieldName, String value);
    public Criteria<E> nlike(String fieldName, String value, MatchMode mathMode);
    public Criteria<E> eqother(String fieldName, String otherField);
    public Criteria<E> neother(String fieldName, String otherField);
    public Criteria<E> ltother(String fieldName, String otherField);
    public Criteria<E> gtother(String fieldName, String otherField);
    public Criteria<E> leother(String fieldName, String otherField);
    public Criteria<E> geother(String fieldName, String otherField);

    /**
     * 设置排序字段
     * @param fieldName
     * @return
     */
    public Criteria<E> orderby(String fieldName);
    /**
     * 设置一个排序字段
     * @param fieldName
     * @param desc
     * @return
     */
    public Criteria<E> orderby(String fieldName, boolean desc);

    /**
     * 设置查询字段
     * @param fieldName
     * @return
     */
    public Criteria<E> field(String fieldName);

    /**
     * 	设置查询字段
     * @param first
     * @param fieldNames
     * @return
     */
    public Criteria<E> fields(String first, String...fieldNames);
}
