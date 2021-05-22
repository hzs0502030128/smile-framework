package org.smile.db.criteria;

import org.smile.lambda.Lambda;
import org.smile.lambda.LambdaUtils;

/**
 * 条件规范
 * 
 * @author 胡真山
 *
 */
public class Restrictions {
	public static final String EQ = "=";
	public static final String NE = "!=";
	public static final String LT = "<";
	public static final String GT = ">";
	public static final String LE = "<=";
	public static final String GE = ">=";
	public static final String LIKE = "like";
	public static final String NOT_LIKE = "not like";
	public static final String BETWEEN = "between";
	public static final String IN = "in";
	public static final String AND = "and";
	public static final String OR = "or";

	/**
	 * 构建一个or条件表达式
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static Criterion or(Criterion left, Criterion right) {
		return new ConditionCriterion(left, right, OR);
	}

	/**
	 * 构建一个and条件表达式
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static Criterion and(Criterion left, Criterion right) {
		return new ConditionCriterion(left, right, AND);
	}

	/**
	 * between 表达式
	 * 
	 * @param fieldName
	 * @param start
	 * @param end
	 * @return
	 */
	public static Criterion between(String fieldName, Object start, Object end) {
		return new BetweenCriterion(fieldName, start, end, BETWEEN);
	}

	/**
	 *  ==
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Criterion eq(String fieldName, Object value) {
		return new SimpleCriterion(fieldName, value, EQ);
	}

	/**
	 * 	!=
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Criterion ne(String fieldName, Object value) {
		return new SimpleCriterion(fieldName, value, NE);
	}

	/**
	 * 	小于
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Criterion lt(String fieldName, Object value) {
		return new SimpleCriterion(fieldName, value, LT);
	}

	/**
	 * 	大于
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Criterion gt(String fieldName, Object value) {
		return new SimpleCriterion(fieldName, value, GT);
	}

	/**
	 * 小于等于
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Criterion le(String fieldName, Object value) {
		return new SimpleCriterion(fieldName, value, LE);
	}

	/**
	 * 大于等于
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Criterion ge(String fieldName, Object value) {
		return new SimpleCriterion(fieldName, value, GE);
	}

	/**
	 *	 在这之中
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Criterion in(String fieldName, Object value) {
		return new SimpleCriterion(fieldName, value, IN);
	}

	public static Criterion like(String fieldName, String value, MatchMode mathMode) {
		return new SimpleCriterion(fieldName, mathMode.toMatchString(value), LIKE);
	}

	public static Criterion like(String fieldName, String value) {
		return new SimpleCriterion(fieldName, MatchMode.ANYWHERE.toMatchString(value), LIKE);
	}

	public static Criterion nlike(String fieldName, String value, MatchMode mathMode) {
		return new SimpleCriterion(fieldName, mathMode.toMatchString(value), NOT_LIKE);
	}

	public static Criterion nlike(String fieldName, String value) {
		return new SimpleCriterion(fieldName, MatchMode.ANYWHERE.toMatchString(value), NOT_LIKE);
	}

	/**
	 * between 表达式
	 * 
	 * @param fieldName
	 * @param start
	 * @param end
	 * @return
	 */
	public static <E> Criterion between(Lambda<E,?> fieldName, Object start, Object end) {
		return new BetweenCriterion(LambdaUtils.getPropertyName(fieldName), start, end, BETWEEN);
	}

	public static <E> Criterion eq(Lambda<E,?> fieldName, Object value) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), value, EQ);
	}

	public static <E> Criterion ne(Lambda<E,?> fieldName, Object value) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), value, NE);
	}

	public static <E> Criterion lt(Lambda<E,?> fieldName, Object value) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), value, LT);
	}

	public static <E> Criterion gt(Lambda<E,?> fieldName, Object value) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), value, GT);
	}

	public static <E> Criterion le(Lambda<E,?> fieldName, Object value) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), value, LE);
	}

	public static <E> Criterion ge(Lambda<E,?> fieldName, Object value) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), value, GE);
	}

	public static <E> Criterion in(Lambda<E,?> fieldName, Object value) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), value, IN);
	}

	public static <E> Criterion like(Lambda<E,?> fieldName, String value, MatchMode mathMode) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), mathMode.toMatchString(value), LIKE);
	}

	public static <E> Criterion like(Lambda<E,?> fieldName, String value) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), MatchMode.ANYWHERE.toMatchString(value), LIKE);
	}

	public static <E> Criterion nlike(Lambda<E,?> fieldName, String value, MatchMode mathMode) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), mathMode.toMatchString(value), NOT_LIKE);
	}

	public static <E> Criterion nlike(Lambda<E,?> fieldName, String value) {
		return new SimpleCriterion(LambdaUtils.getPropertyName(fieldName), MatchMode.ANYWHERE.toMatchString(value), NOT_LIKE);
	}
	
	/**
	 * 与其它字段相同
	 * @param fieldName
	 * @param otherProperty
	 * @return
	 */
	public static  Criterion eqother(String fieldName,String otherProperty) {
		return new OtherFieldCriterion(fieldName, otherProperty, EQ);
	}
	/**
	 * 与其它字段不同
	 * @param fieldName
	 * @param otherProperty
	 * @return
	 */
	public static  Criterion neother(String fieldName,String otherProperty) {
		return new OtherFieldCriterion(fieldName, otherProperty, NE);
	}
	public static  Criterion ltother(String fieldName,String otherProperty) {
		return new OtherFieldCriterion(fieldName, otherProperty, LT);
	}
	public static  Criterion gtother(String fieldName,String otherProperty) {
		return new OtherFieldCriterion(fieldName, otherProperty, GT);
	}
	public static  Criterion leother(String fieldName,String otherProperty) {
		return new OtherFieldCriterion(fieldName, otherProperty, LE);
	}
	public static  Criterion geother(String fieldName,String otherProperty) {
		return new OtherFieldCriterion(fieldName, otherProperty, GE);
	}
	/**
	 *  is null 表达式
	 * @param fieldName
	 * @return
	 */
	public static  Criterion isnull(String fieldName) {
		return new IsNullCriterion(fieldName, true);
	}
	/**
	 * is not null 表达式
	 * @param fieldName
	 * @return
	 */
	public static Criterion notnull(String fieldName) {
		return new IsNullCriterion(fieldName, false);
	}
	
	public static Criterion field(String fieldName) {
		return new FieldCriterion(fieldName);
	}
	
	public static Criterion field(String fieldName,String alias ) {
		return new FieldCriterion(fieldName,alias);
	}

	/**
	 * 与其它字段相同
	 * 
	 * @param fieldName
	 * @param otherProperty
	 * @return
	 */
	public static <E> Criterion eqother(Lambda<E,?> fieldName, Lambda<E,?> otherProperty) {
		return new OtherFieldCriterion(LambdaUtils.getPropertyName(fieldName), LambdaUtils.getPropertyName(otherProperty), EQ);
	}

	/**
	 * 与其它字段不同
	 * 
	 * @param fieldName
	 * @param otherProperty
	 * @return
	 */
	public static <E> Criterion neother(Lambda<E,?> fieldName, Lambda<E,?> otherProperty) {
		return new OtherFieldCriterion(LambdaUtils.getPropertyName(fieldName), LambdaUtils.getPropertyName(otherProperty), NE);
	}

	public static <E> Criterion ltother(Lambda<E,?> fieldName, Lambda<E,?> otherProperty) {
		return new OtherFieldCriterion(LambdaUtils.getPropertyName(fieldName), LambdaUtils.getPropertyName(otherProperty), LT);
	}

	public static <E> Criterion gtother(Lambda<E,?> fieldName, Lambda<E,?> otherProperty) {
		return new OtherFieldCriterion(LambdaUtils.getPropertyName(fieldName), LambdaUtils.getPropertyName(otherProperty), GT);
	}

	public static <E> Criterion leother(Lambda<E,?> fieldName, Lambda<E,?> otherProperty) {
		return new OtherFieldCriterion(LambdaUtils.getPropertyName(fieldName), LambdaUtils.getPropertyName(otherProperty), LE);
	}

	public static <E> Criterion geother(Lambda<E,?> fieldName, Lambda<E,?> otherProperty) {
		return new OtherFieldCriterion(LambdaUtils.getPropertyName(fieldName), LambdaUtils.getPropertyName(otherProperty), GE);
	}

	/**
	 * is null 表达式
	 * 
	 * @param fieldName
	 * @return
	 */
	public static <E> Criterion isnull(Lambda<E,?> fieldName) {
		return new IsNullCriterion(LambdaUtils.getPropertyName(fieldName), true);
	}

	/**
	 * is not null 表达式
	 * 
	 * @param fieldName
	 * @return
	 */
	public static <E> Criterion notnull(Lambda<E,?> fieldName) {
		return new IsNullCriterion(LambdaUtils.getPropertyName(fieldName), false);
	}

	public static <E> Criterion field(Lambda<E,?> fieldName) {
		return new FieldCriterion(LambdaUtils.getPropertyName(fieldName));
	}

	public static <E> Criterion field(Lambda<E,?> fieldName, String alias) {
		return new FieldCriterion(LambdaUtils.getPropertyName(fieldName), alias);
	}
}
