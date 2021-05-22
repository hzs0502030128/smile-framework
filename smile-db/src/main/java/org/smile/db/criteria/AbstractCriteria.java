package org.smile.db.criteria;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.smile.collection.CollectionUtils;
import org.smile.lambda.Lambda;
import org.smile.lambda.LambdaUtils;

public abstract class AbstractCriteria<E> implements Criteria<E> {
	/**
	 * 指定查询条件
	 */
	protected List<Criterion> criterions=new LinkedList<Criterion>();
	/**
	 * 指定排序字段
	 */
	protected List<Criterion> orderby=new LinkedList<Criterion>();
	/**
	 * 指定的查询字段
	 */
	protected List<String> queryFields=new ArrayList<String>();
	/**
	 * 查询分页偏移行
	 */
	protected long offset=0;
	/**
	 * 查询条数限制
	 */
	protected int limit=0;

	@Override
	public Criteria<E> between(String fieldName, Object start, Object end) {
		checkAddAnd();
		criterions.add(Restrictions.between(fieldName, start, end));
		return this;
	}

	@Override
	public Criteria<E> eq(String fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.eq(fieldName, value));
		return this;
	}

	@Override
	public Criteria<E> ne(String fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.ne(fieldName, value));
		return this;
	}

	@Override
	public Criteria<E> lt(String fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.lt(fieldName, value));
		return this;
	}

	@Override
	public Criteria<E> gt(String fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.gt(fieldName, value));
		return this;
	}

	@Override
	public Criteria<E> le(String fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.le(fieldName, value));
		return this;
	}

	@Override
	public Criteria<E> ge(String fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.ge(fieldName, value));
		return this;
	}

	@Override
	public Criteria<E> in(String fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.in(fieldName, value));
		return this;
	}

	@Override
	public Criteria<E> like(String fieldName, String value, MatchMode mathMode) {
		checkAddAnd();
		criterions.add(Restrictions.like(fieldName, value,mathMode));
		return this;
	}

	@Override
	public Criteria<E> like(String fieldName, String value) {
		checkAddAnd();
		criterions.add(Restrictions.like(fieldName, value));
		return this;
	}

	@Override
	public Criteria<E> eqother(String fieldName, String otherField) {
		checkAddAnd();
		criterions.add(Restrictions.eqother(fieldName, otherField));
		return this;
	}

	@Override
	public Criteria<E> neother(String fieldName,String otherField) {
		checkAddAnd();
		criterions.add(Restrictions.neother(fieldName, otherField));
		return this;
	}

	@Override
	public Criteria<E> ltother(String fieldName, String otherField) {
		checkAddAnd();
		criterions.add(Restrictions.ltother(fieldName, otherField));
		return this;
	}

	@Override
	public Criteria<E> gtother(String fieldName, String otherField) {
		checkAddAnd();
		criterions.add(Restrictions.gtother(fieldName, otherField));
		return this;
	}

	@Override
	public Criteria<E> leother(String fieldName, String otherField) {
		checkAddAnd();
		criterions.add(Restrictions.leother(fieldName, otherField));
		return this;
	}

	@Override
	public Criteria<E> geother(String fieldName, String otherField) {
		checkAddAnd();
		criterions.add(Restrictions.geother(fieldName, otherField));
		return this;
	}
	
	@Override
	public LambdaCriteria<E> between(Lambda fieldName, Object start, Object end) {
		checkAddAnd();
		criterions.add(Restrictions.between(fieldName, start, end));
		return this;
	}

	@Override
	public LambdaCriteria<E> eq(Lambda fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.eq(fieldName, value));
		return this;
	}

	@Override
	public LambdaCriteria<E> ne(Lambda fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.ne(fieldName, value));
		return this;
	}

	@Override
	public LambdaCriteria<E> lt(Lambda fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.lt(fieldName, value));
		return this;
	}

	@Override
	public LambdaCriteria<E> gt(Lambda fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.gt(fieldName, value));
		return this;
	}

	@Override
	public LambdaCriteria<E> le(Lambda fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.le(fieldName, value));
		return this;
	}

	@Override
	public LambdaCriteria<E> ge(Lambda fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.ge(fieldName, value));
		return this;
	}

	@Override
	public LambdaCriteria<E> in(Lambda fieldName, Object value) {
		checkAddAnd();
		criterions.add(Restrictions.in(fieldName, value));
		return this;
	}

	@Override
	public LambdaCriteria<E> like(Lambda fieldName, String value, MatchMode mathMode) {
		checkAddAnd();
		criterions.add(Restrictions.like(fieldName, value,mathMode));
		return this;
	}

	@Override
	public LambdaCriteria<E> like(Lambda fieldName, String value) {
		checkAddAnd();
		criterions.add(Restrictions.like(fieldName, value));
		return this;
	}

	@Override
	public LambdaCriteria<E> eqother(Lambda fieldName, Lambda otherField) {
		checkAddAnd();
		criterions.add(Restrictions.eqother(fieldName, otherField));
		return this;
	}

	@Override
	public LambdaCriteria<E> neother(Lambda fieldName,Lambda otherField) {
		checkAddAnd();
		criterions.add(Restrictions.neother(fieldName, otherField));
		return this;
	}

	@Override
	public LambdaCriteria<E> ltother(Lambda fieldName, Lambda otherField) {
		checkAddAnd();
		criterions.add(Restrictions.ltother(fieldName, otherField));
		return this;
	}

	@Override
	public LambdaCriteria<E> gtother(Lambda fieldName, Lambda otherField) {
		checkAddAnd();
		criterions.add(Restrictions.gtother(fieldName, otherField));
		return this;
	}

	@Override
	public LambdaCriteria<E> leother(Lambda fieldName, Lambda otherField) {
		checkAddAnd();
		criterions.add(Restrictions.leother(fieldName, otherField));
		return this;
	}

	@Override
	public LambdaCriteria<E> geother(Lambda fieldName, Lambda otherField) {
		checkAddAnd();
		criterions.add(Restrictions.geother(fieldName, otherField));
		return this;
	}


	@Override
	public Criteria<E> and(Criterion criterion) {
		checkAddAnd();
		criterions.add(criterion);
		return this;
	}

	@Override
	public Criteria<E> or(Criterion criterion) {
		criterions.add(new OperatorCriterion(Restrictions.OR));
		criterions.add(criterion);
		return this;
	}
	/**
	 * 	判断添加AND表达式
	 * @return
	 */
	protected Criteria<E> checkAddAnd() {
		if(criterions.size()>0) {
			criterions.add(new OperatorCriterion(Restrictions.AND));
		}
		return this;
	}

	@Override
	public Criteria<E> orderby(String fieldName) {
		orderby.add(new OrderbyCriterion(fieldName));
		return this;
	}
	
	@Override
	public LambdaCriteria<E> orderby(Lambda fieldName) {
		orderby.add(new OrderbyCriterion(LambdaUtils.getPropertyName(fieldName)));
		return this;
	}

	@Override
	public LambdaCriteria<E> orderby(Lambda fieldName, boolean desc) {
		orderby.add(new OrderbyCriterion(LambdaUtils.getPropertyName(fieldName),desc?"desc":""));
		return this;
	}

	@Override
	public LambdaCriteria<E> isnull(Lambda fieldName) {
		checkAddAnd();
		criterions.add(Restrictions.isnull(fieldName));
		return this;
	}

	@Override
	public LambdaCriteria<E> notnull(Lambda fieldName) {
		checkAddAnd();
		criterions.add(Restrictions.notnull(fieldName));
		return this;
	}

	@Override
	public Criteria<E> field(String fieldName) {
		queryFields.add(fieldName);
		return this;
	}
	
	@Override
	public LambdaCriteria<E> field(Lambda fieldName) {
		queryFields.add(LambdaUtils.getPropertyName(fieldName));
		return this;
	}

	@Override
	public Criteria<E> fields(String first, String... fieldName) {
		queryFields.add(first);
		CollectionUtils.add(queryFields, fieldName);
		return this;
	}
	@Override
	public Criteria<E> orderby(String fieldName, boolean desc) {
		orderby.add(new OrderbyCriterion(fieldName,desc?"desc":""));
		return this;
	}

	@Override
	public Criteria<E> isnull(String fieldName) {
		checkAddAnd();
		criterions.add(Restrictions.isnull(fieldName));
		return this;
	}

	@Override
	public Criteria<E> notnull(String fieldName) {
		checkAddAnd();
		criterions.add(Restrictions.notnull(fieldName));
		return this;
	}

	@Override
	public LambdaCriteria<E> fields(Lambda first, Lambda... fieldName) {
		queryFields.add(LambdaUtils.getPropertyName(first));
		for(Lambda lambda:fieldName) {
			queryFields.add(LambdaUtils.getPropertyName(lambda));
		}
		return this;
	}

	@Override
	public Criteria<E> offset(long offset) {
		this.offset=offset;
		return this;
	}

	@Override
	public Criteria<E> limit(int limit) {
		this.limit=limit;
		return this;
	}
	
	
	
	@Override
	public Criteria<E> nlike(String fieldName, String value) {
		checkAddAnd();
		criterions.add(Restrictions.nlike(fieldName, value));
		return this;
	}

	@Override
	public Criteria<E> nlike(String fieldName, String value, MatchMode mathMode) {
		checkAddAnd();
		criterions.add(Restrictions.nlike(fieldName, value, mathMode));
		return this;
	}
	
	@Override
	public Criteria<E> nlike(Lambda fieldName, String value) {
		checkAddAnd();
		criterions.add(Restrictions.nlike(fieldName, value));
		return this;
	}

	@Override
	public Criteria<E> nlike(Lambda fieldName, String value, MatchMode mathMode) {
		checkAddAnd();
		criterions.add(Restrictions.nlike(fieldName, value, mathMode));
		return this;
	}
	

	@Override
	public Criteria<E> reset() {
		this.criterions.clear();
		this.orderby.clear();
		this.offset=0;
		this.limit=0;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder str=new StringBuilder(64);
		str.append("fields:").append(queryFields).append(",where:").append(this.criterions);
		str.append(",orderby:").append(orderby);
		return super.toString();
	}
	
}
